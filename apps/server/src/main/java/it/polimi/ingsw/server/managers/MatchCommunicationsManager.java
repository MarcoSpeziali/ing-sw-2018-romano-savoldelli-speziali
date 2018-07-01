package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.controllers.proxies.rmi.MatchRMIProxyController;
import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.mocks.IMatch;
import it.polimi.ingsw.net.mocks.IWindow;
import it.polimi.ingsw.net.mocks.MatchMock;
import it.polimi.ingsw.net.mocks.WindowMock;
import it.polimi.ingsw.net.requests.WindowRequest;
import it.polimi.ingsw.net.responses.MatchBeginResponse;
import it.polimi.ingsw.net.responses.MatchEndResponse;
import it.polimi.ingsw.net.responses.MoveResponse;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.events.MatchCommunicationsListener;
import it.polimi.ingsw.server.net.sockets.AuthenticatedClientHandler;
import it.polimi.ingsw.server.net.sockets.middlewares.MatchControllerMiddleware;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.wrap;

public class MatchCommunicationsManager {

    /**
     * A {@link Map} containing the {@link MatchRMIProxyController} associated to a {@link DatabasePlayer}.
     */
    private final Map<DatabasePlayer, MatchRMIProxyController> rmiPlayersHandler = new HashMap<>();

    /**
     * A {@link Map} containing the {@link AuthenticatedClientHandler} associated to a {@link DatabasePlayer}.
     */
    private final Map<DatabasePlayer, AuthenticatedClientHandler> socketPlayersHandler = new HashMap<>();
    private final IMatch match;
    private final MatchCommunicationsListener matchCommunicationsListener;
    
    // ------ LIFE CYCLE ------
    
    public MatchCommunicationsManager(IMatch match, MatchCommunicationsListener matchCommunicationsListener) {
        this.match = match;
        this.matchCommunicationsListener = matchCommunicationsListener;
    }

    public void addPlayer(DatabasePlayer databasePlayer, MatchRMIProxyController matchController) {
        this.rmiPlayersHandler.put(databasePlayer, matchController);
    }
    
    public void addPlayer(DatabasePlayer databasePlayer, AuthenticatedClientHandler authenticatedClientHandler) {
        this.socketPlayersHandler.put(databasePlayer, authenticatedClientHandler);
    }

    public void removePlayer(DatabasePlayer databasePlayer) {
        this.rmiPlayersHandler.remove(databasePlayer);
        this.socketPlayersHandler.remove(databasePlayer);
    }

    public void closeConnectionToOtherPlayers() {
        this.forEachSocket((databasePlayer, authenticatedClientHandler) -> authenticatedClientHandler.addMiddleware(
                MatchControllerMiddleware.class,
                new MatchControllerMiddleware(this)
        ));
        this.forEachRmi((databasePlayer, matchRMIProxyController) -> {
            matchRMIProxyController.setWindowResponseConsumer(window -> this.onWindowChosen(databasePlayer, window));
            matchRMIProxyController.setMoveConsumer(move -> this.onMoveRequested(databasePlayer, move));
            matchRMIProxyController.setEndTurnRunnable(() -> this.onEndRequested(databasePlayer));
        });
    }
    
    // ------ MODELS ------
    
    public void sendWindowsToChoose(Map<DatabasePlayer, IWindow[]> playerToWindowsMap) throws IOException {
        this.forEachRmi((databasePlayer, matchRMIProxyController) -> {
            matchRMIProxyController.postWindowsToChoose(playerToWindowsMap.get(databasePlayer));
            matchRMIProxyController.setWindowResponseConsumer(iWindow -> this.onWindowChosen(databasePlayer, iWindow));
        });

        try {
            this.forEachSocket((databasePlayer, authenticatedClientHandler) -> {
                try {
                    authenticatedClientHandler.sendRequest(new Request<>(
                            new Header(EndPointFunction.MATCH_WINDOW_REQUEST),
                            new WindowRequest(
                                    this.match.getId(),
                                    Arrays.stream(playerToWindowsMap.get(databasePlayer))
                                            .map(WindowMock::new)
                                            .toArray(WindowMock[]::new)
                            )
                    ));
                }
                catch (IOException e) {
                    wrap(e);
                }
            });
        }
        catch (FunctionalExceptionWrapper e) {
            e.tryFinalUnwrap(IOException.class);
        }
    }

    public void sendMatchMockToPlayer(DatabasePlayer databasePlayer, IMatch match) throws IOException {
        try {
            this.rmiPlayersHandler.computeIfPresent(databasePlayer, wrap((player, rmiProxyController) -> {
                rmiProxyController.onUpdateReceived(match);
                return rmiProxyController;
            }));

            this.socketPlayersHandler.computeIfPresent(databasePlayer, wrap((player, authenticatedClientHandler) -> {
                authenticatedClientHandler.sendResponse(new Response<>(
                        new Header(EndPointFunction.MATCH_UPDATE_RESPONSE),
                        new MatchMock(match)
                ));
                return authenticatedClientHandler;
            }));
        }
        catch (FunctionalExceptionWrapper e) {
            e.tryUnwrap(RemoteException.class).tryFinalUnwrap(IOException.class);
        }
    }
    
    // ------ TURNS ------
    
    public void notifyPlayerTurnBegin(DatabasePlayer player, int timeToCompleteInSeconds) throws IOException {
        try {
            this.rmiPlayersHandler.computeIfPresent(player, wrap((databasePlayer, matchRMIProxyController) -> {
                matchRMIProxyController.postTurnBegin(timeToCompleteInSeconds);
                return matchRMIProxyController;
            }));
            
            this.socketPlayersHandler.computeIfPresent(player, wrap((databasePlayer, authenticatedClientHandler) -> {
                authenticatedClientHandler.sendResponse(new Response<>(
                        new Header(EndPointFunction.MATCH_PLAYER_TURN_BEGIN_RESPONSE),
                        new MatchBeginResponse(
                                this.match.getId(),
                                timeToCompleteInSeconds
                        )
                ));
                
                return authenticatedClientHandler;
            }));
        }
        catch (FunctionalExceptionWrapper e) {
            e.tryUnwrap(RemoteException.class)
                    .tryFinalUnwrap(IOException.class);
        }
    }
    
    public void notifyPlayerTurnEnd(DatabasePlayer player) throws IOException {
        try {
            this.rmiPlayersHandler.computeIfPresent(player, wrap((databasePlayer, matchRMIProxyController) -> {
                matchRMIProxyController.postTurnEnd();
                return matchRMIProxyController;
            }));
        
            this.socketPlayersHandler.computeIfPresent(player, wrap((databasePlayer, authenticatedClientHandler) -> {
                authenticatedClientHandler.sendResponse(new Response<>(
                        new Header(EndPointFunction.MATCH_PLAYER_TURN_END_RESPONSE),
                        new MatchEndResponse(
                                this.match.getId()
                        )
                ));
            
                return authenticatedClientHandler;
            }));
        }
        catch (FunctionalExceptionWrapper e) {
            e.tryUnwrap(RemoteException.class)
                    .tryFinalUnwrap(IOException.class);
        }
    }
    
    // ------ MOVE ------
    
    public void sendPositiveResponseForMoveToPlayer(DatabasePlayer databasePlayer) throws IOException {
        this.sendMoveResponse(databasePlayer, new MoveResponse(
                this.match.getId(),
                true
        ));
    }
    
    public void sendNegativeResponseForMoveToPlayer(DatabasePlayer databasePlayer) throws IOException {
        this.sendMoveResponse(databasePlayer, new MoveResponse(
                this.match.getId(),
                false
        ));
    }
    
    private void sendMoveResponse(DatabasePlayer databasePlayer, MoveResponse moveResponse) throws IOException {
        try {
            this.rmiPlayersHandler.computeIfPresent(databasePlayer, wrap((player, rmiProxyController) -> {
                rmiProxyController.postMoveResponse(moveResponse);
                return rmiProxyController;
            }));

            this.socketPlayersHandler.computeIfPresent(databasePlayer, wrap((player, authenticatedClientHandler) -> {
                authenticatedClientHandler.sendResponse(new Response<>(
                        new Header(EndPointFunction.MATCH_PLAYER_MOVE_RESPONSE),
                        moveResponse
                ));
                return authenticatedClientHandler;
            }));
        }
        catch (FunctionalExceptionWrapper e) {
            e.tryUnwrap(RemoteException.class).tryFinalUnwrap(IOException.class);
        }
    }

    // ------ TOOL CARDS ------

    // ------ RESULTS ------

    public void sendResults(Map<DatabasePlayer, Integer> results) {

    }
    
    // ------ UTILS ------
    
    public void forEachRmi(BiConsumer<DatabasePlayer, MatchRMIProxyController> biConsumer) {
        this.rmiPlayersHandler.forEach(biConsumer);
    }

    public void forEachSocket(BiConsumer<DatabasePlayer, AuthenticatedClientHandler> biConsumer) {
        this.socketPlayersHandler.forEach(biConsumer);
    }
    
    // ------ EXTERNAL EVENTS ------

    public void onWindowChosen(DatabasePlayer databasePlayer, IWindow chosenWindow) {
        this.matchCommunicationsListener.onWindowChosen(this, databasePlayer, chosenWindow);
    }
    
    public void onMoveRequested(DatabasePlayer databasePlayer, Move requestedMove) {
        this.matchCommunicationsListener.onPlayerTriedToMove(this, databasePlayer, requestedMove);
    }
    
    public void onEndRequested(DatabasePlayer databasePlayer) {
        this.matchCommunicationsListener.onPlayerEndRequest(this, databasePlayer);
    }
}
