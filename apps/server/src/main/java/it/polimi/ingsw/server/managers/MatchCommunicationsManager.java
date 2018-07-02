package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.controllers.proxies.rmi.MatchRMIProxyController;
import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.core.ToolCardConditionException;
import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.mocks.*;
import it.polimi.ingsw.net.requests.WindowRequest;
import it.polimi.ingsw.net.responses.MatchBeginResponse;
import it.polimi.ingsw.net.responses.MatchEndResponse;
import it.polimi.ingsw.net.responses.MoveResponse;
import it.polimi.ingsw.net.responses.ResultsResponse;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.constraints.ConstraintEvaluationException;
import it.polimi.ingsw.server.events.MatchCommunicationsListener;
import it.polimi.ingsw.server.net.sockets.AuthenticatedClientHandler;
import it.polimi.ingsw.server.net.sockets.middlewares.MatchControllerMiddleware;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.utils.Range;
import it.polimi.ingsw.utils.io.json.JSONSerializable;
import it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
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
            matchRMIProxyController.setToolCardConsumer(toolCard -> this.onToolCardRequested(databasePlayer, toolCard));
        });
    }
    
    // ------ MODELS ------
    
    public void sendWindowsToChoose(Map<DatabasePlayer, IWindow[]> playerToWindowsMap) throws IOException {
        this.forEachRmi((databasePlayer, matchRMIProxyController) -> matchRMIProxyController.postWindowsToChoose(playerToWindowsMap.get(databasePlayer)));

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
        this.sendToPlayer(databasePlayer, (authenticatedClientHandler, matchRMIProxyController) -> {
            if (authenticatedClientHandler != null) {
                authenticatedClientHandler.sendResponse(new Response<>(
                        new Header(EndPointFunction.MATCH_UPDATE_RESPONSE),
                        new MatchMock(match)
                ));
            }
            else {
                matchRMIProxyController.onUpdateReceived(match);
            }
        });
    }
    
    // ------ TURNS ------
    
    public void notifyPlayerTurnBegin(DatabasePlayer player, int timeToCompleteInSeconds) throws IOException {
        this.sendToPlayer(player, (authenticatedClientHandler, matchRMIProxyController) -> {
            if (authenticatedClientHandler != null) {
                authenticatedClientHandler.sendResponse(new Response<>(
                        new Header(EndPointFunction.MATCH_PLAYER_TURN_BEGIN_RESPONSE),
                        new MatchBeginResponse(
                                this.match.getId(),
                                timeToCompleteInSeconds
                        )
                ));
            }
            else {
                matchRMIProxyController.postTurnBegin(timeToCompleteInSeconds);
            }
        });
    }
    
    public void notifyPlayerTurnEnd(DatabasePlayer player) throws IOException {
        this.sendToPlayer(player, (authenticatedClientHandler, matchRMIProxyController) -> {
            if (authenticatedClientHandler != null) {
                authenticatedClientHandler.sendResponse(new Response<>(
                        new Header(EndPointFunction.MATCH_PLAYER_TURN_END_RESPONSE),
                        new MatchEndResponse(
                                this.match.getId()
                        )
                ));
            }
            else {
                matchRMIProxyController.postTurnEnd();
            }
        });
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
        this.sendToPlayer(databasePlayer, (authenticatedClientHandler, matchRMIProxyController) -> {
            if (authenticatedClientHandler != null) {
                authenticatedClientHandler.sendResponse(new Response<>(
                        new Header(EndPointFunction.MATCH_PLAYER_MOVE_REQUEST),
                        moveResponse
                ));
            }
            else {
                matchRMIProxyController.postMoveResponse(moveResponse);
            }
        });
    }

    // ------ TOOL CARDS ------
    
    public Integer sendChoosePosition(DatabasePlayer databasePlayer, JSONSerializable jsonSerializable, Set<Integer> unavailableLocations) throws IOException {
        return 0;
    }

    public Integer sendChooseShade(DatabasePlayer databasePlayer, IDie die) throws IOException {
        return 0;
    }
    
    public boolean sendShouldRepeat(IAction action, int alreadyRepeatedFor, int maximumRepetitions) throws IOException {
        return false;
    }
    
    public List<IAction> sendChooseActions(List<IAction> actions, Range<Integer> chooseBetween) throws IOException {
        return null;
    }
    
    // ------ RESULTS ------

    public void sendResults(Map<DatabasePlayer, Integer> resultMap) throws IOException {
        IResult[] results = resultMap.entrySet().stream()
                .map(entry -> new ResultMock(this.match.getId(), new PlayerMock(entry.getKey()), entry.getValue()))
                .toArray(IResult[]::new);
    
        try {
            this.forEachRmi(wrap((databasePlayer, rmiProxyController) -> {
                rmiProxyController.postResults(results);
            }));
            this.forEachSocket(wrap((databasePlayer, authenticatedClientHandler) -> {
                authenticatedClientHandler.sendResponse(
                        new Response<>(
                                new Header(EndPointFunction.MATCH_RESULTS_RESPONSE),
                                new ResultsResponse(
                                        this.match.getId(),
                                        resultMap.entrySet().stream()
                                                .map(entry ->
                                                        new ResultMock(
                                                                this.match.getId(),
                                                                new PlayerMock(entry.getKey()),
                                                                entry.getValue()
                                                        )
                                                ).toArray(ResultMock[]::new)
                                )
                        )
                );
            }));
        }
        catch (FunctionalExceptionWrapper e) {
            e.tryUnwrap(RemoteException.class).tryFinalUnwrap(IOException.class);
        }
    }
    
    // ------ UTILS ------
    
    public void forEachRmi(BiConsumer<DatabasePlayer, MatchRMIProxyController> biConsumer) {
        this.rmiPlayersHandler.forEach(biConsumer);
    }

    public void forEachSocket(BiConsumer<DatabasePlayer, AuthenticatedClientHandler> biConsumer) {
        this.socketPlayersHandler.forEach(biConsumer);
    }
    
    public void sendToPlayer(DatabasePlayer databasePlayer, FunctionalExceptionWrapper.UnsafeBiConsumer<AuthenticatedClientHandler, MatchRMIProxyController> biConsumer) throws IOException {
        try {
            this.rmiPlayersHandler.computeIfPresent(databasePlayer, wrap((player, rmiProxyController) -> {
                biConsumer.accept(null, rmiProxyController);
                return rmiProxyController;
            }));
        
            this.socketPlayersHandler.computeIfPresent(databasePlayer, wrap((player, authenticatedClientHandler) -> {
                biConsumer.accept(authenticatedClientHandler, null);
                return authenticatedClientHandler;
            }));
        }
        catch (FunctionalExceptionWrapper e) {
            e.tryUnwrap(RemoteException.class)
                    .tryFinalUnwrap(IOException.class);
        }
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
    
    public void onToolCardRequested(DatabasePlayer databasePlayer, IToolCard toolCard) {
        try {
            this.matchCommunicationsListener.onPlayerRequestedToolCard(this, databasePlayer, toolCard);
        }
        catch (ConstraintEvaluationException e) {
            throw new ToolCardConditionException();
        }
    }
}
