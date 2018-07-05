package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.controllers.NotEnoughTokensException;
import it.polimi.ingsw.controllers.proxies.rmi.MatchRMIProxyController;
import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.mocks.*;
import it.polimi.ingsw.net.requests.*;
import it.polimi.ingsw.net.responses.*;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // ------ LOCK OBJECTS ------

    private final Object choosePositionSyncObject = new Object();
    private Integer choosePositionForLocationResponse;

    private final Object chooseShadeSyncObject = new Object();
    private Integer chooseShadeResponse;

    private final Object chooseBetweenActionsSyncObject = new Object();
    private IAction[] chooseBetweenActionsResponse;

    private final Object shouldRepeatSyncObject = new Object();
    private Boolean shouldRepeatResponse;

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

            matchRMIProxyController.setMoveFunction(move -> this.onMoveRequested(databasePlayer, move));

            matchRMIProxyController.setEndTurnRunnable(() -> this.onEndRequested(databasePlayer));

            matchRMIProxyController.setToolCardConsumer(toolCard -> this.onToolCardRequested(databasePlayer, toolCard));
            matchRMIProxyController.setChoosePositionConsumer(this::onPositionChosen);
            matchRMIProxyController.setChooseBetweenActionsConsumer(this::onActionsChosen);
            matchRMIProxyController.setSetShadeConsumer(this::onShadeChosen);
            matchRMIProxyController.setShouldRepeatConsumer(this::onShouldRepeatResponse);
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

    // ------ TOOL CARDS ------
    
    public Integer sendChoosePosition(DatabasePlayer databasePlayer, JSONSerializable jsonSerializable, Set<Integer> unavailableLocations) throws IOException, InterruptedException {
        synchronized (choosePositionSyncObject) {
            this.sendToPlayer(databasePlayer, (authenticatedClientHandler, rmiProxyController) -> {
                ChoosePositionForLocationRequest choosePositionForLocationRequest = new ChoosePositionForLocationRequest(
                        this.match.getId(),
                        jsonSerializable,
                        unavailableLocations
                );

                if (authenticatedClientHandler != null) {
                    authenticatedClientHandler.sendRequest(new Request<>(
                            new Header(EndPointFunction.MATCH_PLAYER_TOOL_CARD_CHOOSE_POSITION_REQUEST),
                            choosePositionForLocationRequest
                    ));
                }
                else {
                    rmiProxyController.postChoosePositionForLocation(choosePositionForLocationRequest);
                }
            });

            while (choosePositionForLocationResponse == null) {
                choosePositionSyncObject.wait();
            }

            Integer temp = choosePositionForLocationResponse;
            choosePositionForLocationResponse = null;
            return temp;
        }
    }

    public Integer sendChooseShade(DatabasePlayer databasePlayer, IDie die) throws IOException, InterruptedException {
        synchronized (chooseShadeSyncObject) {

            this.sendToPlayer(databasePlayer, (authenticatedClientHandler, rmiProxyController) -> {
                SetShadeRequest setShadeRequest = new SetShadeRequest(
                        this.match.getId(),
                        new DieMock(die)
                );

                if (authenticatedClientHandler != null) {
                    authenticatedClientHandler.sendRequest(new Request<>(
                            new Header(EndPointFunction.MATCH_PLAYER_TOOL_CARD_CHOOSE_SHADE_REQUEST),
                            setShadeRequest
                    ));
                }
                else {
                    rmiProxyController.postSetShade(setShadeRequest);
                }
            });

            while (chooseShadeResponse == null) {
                chooseShadeSyncObject.wait();
            }

            Integer temp = chooseShadeResponse;
            chooseShadeResponse = null;
            return temp;
        }
    }
    
    public boolean sendShouldRepeat(DatabasePlayer databasePlayer, IAction action) throws IOException, InterruptedException {
        synchronized (shouldRepeatSyncObject) {

            this.sendToPlayer(databasePlayer, (authenticatedClientHandler, rmiProxyController) -> {
                ShouldRepeatRequest shouldRepeatRequest = new ShouldRepeatRequest(
                        this.match.getId(),
                        new ActionMock(action)
                );

                if (authenticatedClientHandler != null) {
                    authenticatedClientHandler.sendRequest(new Request<>(
                            new Header(EndPointFunction.MATCH_PLAYER_TOOL_CARD_SHOULD_CONTINUE_TO_REPEAT_REQUEST),
                            shouldRepeatRequest
                    ));
                }
                else {
                    rmiProxyController.postShouldRepeat(shouldRepeatRequest);
                }
            });

            while (shouldRepeatResponse == null) {
                shouldRepeatSyncObject.wait();
            }

            Boolean temp = shouldRepeatResponse;
            shouldRepeatResponse = null;
            return temp;
        }
    }
    
    public List<IAction> sendChooseActions(DatabasePlayer databasePlayer, ActionMock[] actions, Range<Integer> chooseBetween) throws IOException, InterruptedException {
        synchronized (chooseBetweenActionsSyncObject) {
            this.sendToPlayer(databasePlayer, (authenticatedClientHandler, rmiProxyController) -> {
                ChooseBetweenActionsRequest chooseBetweenActionsRequest = new ChooseBetweenActionsRequest(
                        this.match.getId(),
                        actions,
                        chooseBetween
                );

                if (authenticatedClientHandler != null) {
                    authenticatedClientHandler.sendRequest(new Request<>(
                            new Header(EndPointFunction.MATCH_PLAYER_TOOL_CARD_CHOOSE_BETWEEN_ACTIONS_REQUEST),
                            chooseBetweenActionsRequest
                    ));
                }
                else {
                    rmiProxyController.postChooseBetweenActions(chooseBetweenActionsRequest);
                }
            });

            while (chooseBetweenActionsResponse == null) {
                chooseBetweenActionsSyncObject.wait();
            }

            IAction[] temp = chooseBetweenActionsResponse;
            chooseBetweenActionsResponse = null;
            return Arrays.asList(temp);
        }
    }

    public void sendNotEnoughTokensException(DatabasePlayer databasePlayer, NotEnoughTokensException e) throws IOException {
        this.sendToPlayer(databasePlayer, (authenticatedClientHandler, rmiProxyController) -> {
            NotEnoughTokensResponse r = new NotEnoughTokensResponse(
                    this.match.getId(),
                    e.getRequiredTokens(),
                    e.getCurrentTokensCount()
            );

            Response<NotEnoughTokensResponse> response = new Response<>(
                    new Header(EndPointFunction.MATCH_PLAYER_TOOL_CARD_REQUEST),
                    r
            );

            if (authenticatedClientHandler != null) {
                authenticatedClientHandler.sendResponse(response);
            }
            else {
                rmiProxyController.setToolCardResponse(response);
            }
        });
    }

    public void sendConstraintNotMetException(DatabasePlayer databasePlayer) throws IOException {
        this.sendToPlayer(databasePlayer, (authenticatedClientHandler, rmiProxyController) -> {
            ConstraintNotMetResponse r = new ConstraintNotMetResponse(
                    this.match.getId()
            );

            Response<ConstraintNotMetResponse> response = new Response<>(
                    new Header(EndPointFunction.MATCH_PLAYER_TOOL_CARD_REQUEST),
                    r
            );

            if (authenticatedClientHandler != null) {
                authenticatedClientHandler.sendResponse(response);
            }
            else {
                rmiProxyController.setToolCardResponse(response);
            }
        });
    }

    public void sendToolCardRequestAccepted(DatabasePlayer databasePlayer) throws IOException {
        this.sendToPlayer(databasePlayer, (authenticatedClientHandler, rmiProxyController) -> {
            ToolCardUsageResponse r = new ToolCardUsageResponse(
                    this.match.getId()
            );

            Response<ToolCardUsageResponse> response = new Response<>(
                    new Header(EndPointFunction.MATCH_PLAYER_TOOL_CARD_REQUEST),
                    r
            );

            if (authenticatedClientHandler != null) {
                authenticatedClientHandler.sendResponse(response);
            }
            else {
                rmiProxyController.setToolCardResponse(response);
            }
        });
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
    
    public MoveResponse onMoveRequested(DatabasePlayer databasePlayer, Move requestedMove) {
        return this.matchCommunicationsListener.onPlayerTriedToMove(this, databasePlayer, requestedMove);
    }
    
    public void onEndRequested(DatabasePlayer databasePlayer) {
        this.matchCommunicationsListener.onPlayerEndRequest(this, databasePlayer);
    }
    
    public void onToolCardRequested(DatabasePlayer databasePlayer, IToolCard toolCard) {
        executorService.submit((Callable<Void>) () -> {
            try {
                this.matchCommunicationsListener.onPlayerRequestedToolCard(
                        this,
                        databasePlayer,
                        toolCard
                );
            }
            catch (NotEnoughTokensException e) {
                this.sendNotEnoughTokensException(databasePlayer, e);
                return null;
            }
            catch (ConstraintEvaluationException e) {
                this.sendConstraintNotMetException(databasePlayer);
                return null;
            }

            this.sendToolCardRequestAccepted(databasePlayer);

            return null;
        });
    }

    public void onPositionChosen(Integer position) {
        synchronized (choosePositionSyncObject) {
            this.choosePositionForLocationResponse = position;
            choosePositionSyncObject.notifyAll();
        }
    }

    public void onShadeChosen(Integer shade) {
        synchronized (chooseShadeSyncObject) {
            this.chooseShadeResponse = shade;
            chooseShadeSyncObject.notifyAll();
        }
    }

    public void onActionsChosen(IAction[] actions) {
        synchronized (chooseBetweenActionsSyncObject) {
            this.chooseBetweenActionsResponse = actions;
            chooseBetweenActionsSyncObject.notifyAll();
        }
    }

    public void onShouldRepeatResponse(Boolean shouldRepeat) {
        synchronized (shouldRepeatSyncObject) {
            this.shouldRepeatResponse = shouldRepeat;
            shouldRepeatSyncObject.notifyAll();
        }
    }
}
