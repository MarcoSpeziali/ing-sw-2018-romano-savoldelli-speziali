package it.polimi.ingsw.client.controllers.proxies.socket;

import it.polimi.ingsw.client.net.providers.PersistentSocketInteractionProvider;
import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.controllers.NotEnoughTokensException;
import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.core.ToolCardConditionException;
import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.mocks.*;
import it.polimi.ingsw.net.requests.*;
import it.polimi.ingsw.net.responses.*;
import it.polimi.ingsw.net.utils.EndPointFunction;

import java.io.IOException;
import java.util.Arrays;

public class MatchSocketProxyController implements MatchController {
    
    private static final long serialVersionUID = -3114652530748406372L;
    
    private final String clientToken;
    private transient PersistentSocketInteractionProvider persistentSocketInteractionProvider;
    private Integer matchId;
    
    public MatchSocketProxyController(PersistentSocketInteractionProvider persistentSocketInteractionProvider, String clientToken) {
        this.clientToken = clientToken;
        this.persistentSocketInteractionProvider = persistentSocketInteractionProvider;
    }
    
    @Override
    public void init(Object... args) throws IOException {
        this.matchId = (Integer) args[0];
        
        this.persistentSocketInteractionProvider.postResponse(new Response<>(
                new Header(this.clientToken, EndPointFunction.MATCH_MIGRATION),
                new MigrationResponse(this.matchId)
        ));

        this.persistentSocketInteractionProvider.listenForRequest(
                EndPointFunction.MATCH_WINDOW_REQUEST,
                request -> {
                    synchronized (this.windowsToChooseSyncObject) {
                        @SuppressWarnings("unchecked")
                        Request<WindowRequest> windowRequest = (Request<WindowRequest>) request;
                        this.windowsToChoose = windowRequest.getBody().getWindows();
    
                        this.windowsToChooseSyncObject.notifyAll();
                    }
                }
        );
        
        this.persistentSocketInteractionProvider.listenFor(
                EndPointFunction.MATCH_UPDATE_RESPONSE,
                request -> {
                    synchronized (this.updateSyncObject) {
                        @SuppressWarnings("unchecked")
                        Response<IMatch> windowRequest = (Response<IMatch>) request;
                        this.update = windowRequest.getBody();
                        
                        this.updateSyncObject.notifyAll();
                    }
                }
        );
        
        this.persistentSocketInteractionProvider.listenFor(
                EndPointFunction.MATCH_PLAYER_TURN_BEGIN_RESPONSE,
                response -> {
                    @SuppressWarnings("unchecked")
                    Response<MatchBeginResponse> matchBeginResponse = (Response<MatchBeginResponse>) response;

                    synchronized (beginTurnSyncObject) {
                        this.timeInSeconds = matchBeginResponse.getBody().getTimeRemaining();
                        beginTurnSyncObject.notifyAll();
                    }
                }
        );
        
        this.persistentSocketInteractionProvider.listenFor(
                EndPointFunction.MATCH_PLAYER_TURN_END_RESPONSE,
                response -> {
                    synchronized (endTurnSyncObject) {
                        this.isEnded = true;
                        endTurnSyncObject.notifyAll();
                    }
                }
        );

        this.persistentSocketInteractionProvider.listenFor(
                EndPointFunction.MATCH_RESULTS_RESPONSE,
                response -> {
                    @SuppressWarnings("unchecked")
                    Response<ResultsResponse> matchResultsResponse = (Response<ResultsResponse>) response;

                    synchronized (resultsSyncObject) {
                        this.results = matchResultsResponse.getBody().getResults();

                        resultsSyncObject.notifyAll();
                    }
                }
        );
        
        this.persistentSocketInteractionProvider.listenForRequest(
                EndPointFunction.MATCH_PLAYER_TOOL_CARD_CHOOSE_POSITION_REQUEST,
                request -> {
                    @SuppressWarnings("unchecked")
                    Request<ChoosePositionForLocationRequest> choosePositionForLocationRequest = (Request<ChoosePositionForLocationRequest>) request;
                    
                    synchronized (choosePositionSyncObject) {
                        this.choosePositionRequest = choosePositionForLocationRequest.getBody();
                        
                        this.choosePositionSyncObject.notifyAll();
                    }
                }
        );

        this.persistentSocketInteractionProvider.listenForRequest(
                EndPointFunction.MATCH_PLAYER_TOOL_CARD_CHOOSE_BETWEEN_ACTIONS_REQUEST,
                request -> {
                    @SuppressWarnings("unchecked")
                    Request<ChooseBetweenActionsRequest> chooseBetweenActionsRequest = (Request<ChooseBetweenActionsRequest>) request;

                    synchronized (chooseBetweenActionsSyncObject) {
                        this.chooseBetweenActionsRequest = chooseBetweenActionsRequest.getBody();

                        this.chooseBetweenActionsSyncObject.notifyAll();
                    }
                }
        );

        this.persistentSocketInteractionProvider.listenForRequest(
                EndPointFunction.MATCH_PLAYER_TOOL_CARD_CHOOSE_SHADE_REQUEST,
                request -> {
                    @SuppressWarnings("unchecked")
                    Request<SetShadeRequest> setShadeRequest = (Request<SetShadeRequest>) request;

                    synchronized (setShadeSyncObject) {
                        this.setShadeRequest = setShadeRequest.getBody();

                        this.setShadeSyncObject.notifyAll();
                    }
                }
        );

        this.persistentSocketInteractionProvider.listenForRequest(
                EndPointFunction.MATCH_PLAYER_TOOL_CARD_SHOULD_CONTINUE_TO_REPEAT_REQUEST,
                request -> {
                    @SuppressWarnings("unchecked")
                    Request<ShouldRepeatRequest> shouldRepeatRequest = (Request<ShouldRepeatRequest>) request;

                    synchronized (shouldRepeatSyncObject) {
                        this.shouldRepeatRequest = shouldRepeatRequest.getBody();

                        this.shouldRepeatSyncObject.notifyAll();
                    }
                }
        );
    }
    
    private final transient Object windowsToChooseSyncObject = new Object();
    private IWindow[] windowsToChoose;
    
    @Override
    public IWindow[] waitForWindowRequest() throws InterruptedException {
        synchronized (this.windowsToChooseSyncObject) {
            while (this.windowsToChoose == null) {
                this.windowsToChooseSyncObject.wait();
            }
        
            return this.windowsToChoose;
        }
    }
    
    @Override
    public void respondToWindowRequest(IWindow window) throws IOException {
        this.persistentSocketInteractionProvider.postResponse(
                new Response<>(
                        new Header(
                                this.clientToken,
                                EndPointFunction.MATCH_WINDOW_RESPONSE
                        ),
                        new WindowResponse(
                                this.matchId,
                                new WindowMock(window)
                        )
                )
        );
    }
    
    private final transient Object updateSyncObject = new Object();
    private IMatch update;
    
    @Override
    public IMatch waitForUpdate() throws InterruptedException {
        //noinspection Duplicates
        synchronized (this.updateSyncObject) {
            while (this.update == null) {
                this.updateSyncObject.wait();
            }
        
            IMatch matchUpdate = this.update;
            this.update = null;
            return matchUpdate;
        }
    }
    
    private final transient Object beginTurnSyncObject = new Object();
    private Integer timeInSeconds;
    
    @Override
    public int waitForTurnToBegin() throws IOException, InterruptedException {
        //noinspection Duplicates
        synchronized (beginTurnSyncObject) {
            while (this.timeInSeconds == null) {
                beginTurnSyncObject.wait();
            }
        
            int temp = this.timeInSeconds;
            this.timeInSeconds = null;
            return temp;
        }
    }
    
    @Override
    public void endTurn() throws IOException {
        this.persistentSocketInteractionProvider.postRequest(new Request<>(
                new Header(this.clientToken, EndPointFunction.MATCH_PLAYER_TURN_END_REQUEST),
                new MatchEndRequest(this.matchId)
        ));
    }
    
    private final transient Object endTurnSyncObject = new Object();
    private boolean isEnded = false;
    
    @Override
    public void waitForTurnToEnd() throws IOException, InterruptedException {
        //noinspection Duplicates
        synchronized (endTurnSyncObject) {
            while (!isEnded) {
                endTurnSyncObject.wait();
            }
            
            this.isEnded = false;
        }
    }
    
    @Override
    public MoveResponse tryToMove(Move move) throws IOException {
        return (MoveResponse) this.persistentSocketInteractionProvider.getSyncResponseFor(
                new Request<>(
                        new Header(
                                this.clientToken,
                                EndPointFunction.MATCH_PLAYER_MOVE_REQUEST
                        ),
                        new MoveRequest(
                                this.matchId,
                                move
                        )
                )
        ).getBody();
    }
    
    @Override
    public void requestToolCardUsage(IToolCard toolCard) throws IOException {
        Response<?> response = this.persistentSocketInteractionProvider.getSyncResponseFor(
                new Request<>(
                        new Header(
                                this.clientToken,
                                EndPointFunction.MATCH_PLAYER_TOOL_CARD_REQUEST
                        ),
                        new ToolCardUsageRequest(
                                this.matchId,
                                new ToolCardMock(toolCard)
                        )
                )
        );

        if (response.getBody() instanceof NotEnoughTokensResponse) {
            throw new NotEnoughTokensException(
                    ((NotEnoughTokensResponse) response.getBody()).getRequiredTokens(),
                    ((NotEnoughTokensResponse) response.getBody()).getCurrentTokens()
            );
        }
        else if (response.getBody() instanceof ConstraintNotMetResponse) {
            throw new ToolCardConditionException();
        }
    }
    
    private final transient Object choosePositionSyncObject = new Object();
    private ChoosePositionForLocationRequest choosePositionRequest;
    
    @Override
    public ChoosePositionForLocationRequest waitForChoosePositionFromLocation() throws InterruptedException {
        //noinspection Duplicates
        synchronized (choosePositionSyncObject) {
            while (choosePositionRequest == null) {
                choosePositionSyncObject.wait();
            }
        
            ChoosePositionForLocationRequest temp = this.choosePositionRequest;
            this.choosePositionRequest = null;
            return temp;
        }
    }
    
    @Override
    public void postChosenPosition(Integer chosenPosition) throws IOException {
        this.persistentSocketInteractionProvider.postResponse(
                new Response<>(
                        new Header(
                                this.clientToken,
                                EndPointFunction.MATCH_PLAYER_TOOL_CARD_CHOOSE_POSITION_RESPONSE
                        ),
                        new ChoosePositionForLocationResponse(
                                this.matchId,
                                chosenPosition
                        )
                )
        );
    }

    private final transient Object chooseBetweenActionsSyncObject = new Object();
    private ChooseBetweenActionsRequest chooseBetweenActionsRequest;

    @Override
    public ChooseBetweenActionsRequest waitForChooseBetweenActions() throws InterruptedException {
        //noinspection Duplicates
        synchronized (chooseBetweenActionsSyncObject) {
            while (chooseBetweenActionsRequest == null) {
                chooseBetweenActionsSyncObject.wait();
            }

            ChooseBetweenActionsRequest temp = this.chooseBetweenActionsRequest;
            this.chooseBetweenActionsRequest = null;
            return temp;
        }
    }

    @Override
    public void postChosenActions(IAction[] actions) throws IOException {
        this.persistentSocketInteractionProvider.postResponse(new Response<>(
                new Header(this.clientToken, EndPointFunction.MATCH_PLAYER_TOOL_CARD_CHOOSE_BETWEEN_ACTIONS_RESPONSE),
                new ChooseBetweenActionsResponse(
                        this.matchId,
                        Arrays.stream(actions).map(ActionMock::new).toArray(ActionMock[]::new)
                )
        ));
    }

    private final transient Object shouldRepeatSyncObject = new Object();
    private ShouldRepeatRequest shouldRepeatRequest;

    @Override
    public IAction waitForContinueToRepeat() throws InterruptedException {
        //noinspection Duplicates
        synchronized (shouldRepeatSyncObject) {
            while (shouldRepeatRequest == null) {
                shouldRepeatSyncObject.wait();
            }

            ShouldRepeatRequest temp = this.shouldRepeatRequest;
            this.shouldRepeatRequest = null;
            return temp.getAction();
        }
    }

    @Override
    public void postContinueToRepeatChoice(boolean continueToRepeat) throws IOException {
        this.persistentSocketInteractionProvider.postResponse(new Response<>(
                new Header(this.clientToken, EndPointFunction.MATCH_PLAYER_TOOL_CARD_SHOULD_CONTINUE_TO_REPEAT_RESPONSE),
                new ShouldRepeatResponse(
                        this.matchId,
                        continueToRepeat
                )
        ));
    }

    private final transient Object setShadeSyncObject = new Object();
    private SetShadeRequest setShadeRequest;

    @Override
    public IDie waitForSetShade() throws InterruptedException {
        //noinspection Duplicates
        synchronized (setShadeSyncObject) {
            while (setShadeRequest == null) {
                setShadeSyncObject.wait();
            }

            SetShadeRequest temp = this.setShadeRequest;
            this.setShadeRequest = null;
            return temp.getDie();
        }
    }

    @Override
    public void postSetShade(Integer shade) throws IOException {
        this.persistentSocketInteractionProvider.postResponse(new Response<>(
                new Header(this.clientToken, EndPointFunction.MATCH_PLAYER_TOOL_CARD_CHOOSE_SHADE_RESPONSE),
                new SetShadeResponse(
                        this.matchId,
                        shade
                )
        ));
    }

    private final transient Object resultsSyncObject = new Object();
    private IResult[] results;
    
    @Override
    public IResult[] waitForMatchToEnd() throws InterruptedException {
        //noinspection Duplicates
        synchronized (resultsSyncObject) {
            while (this.results == null) {
                this.resultsSyncObject.wait();
            }

            IResult[] temp = this.results;
            this.results = null;
            return temp;
        }
    }
    
    @Override
    public void close(Object... args) throws IOException {
        this.persistentSocketInteractionProvider.close();
    }
}
