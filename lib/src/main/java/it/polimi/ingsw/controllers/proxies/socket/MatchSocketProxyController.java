package it.polimi.ingsw.controllers.proxies.socket;

import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.controllers.NotEnoughTokensException;
import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.mocks.*;
import it.polimi.ingsw.net.providers.PersistentSocketInteractionProvider;
import it.polimi.ingsw.net.requests.MatchEndRequest;
import it.polimi.ingsw.net.requests.MoveRequest;
import it.polimi.ingsw.net.requests.WindowRequest;
import it.polimi.ingsw.net.responses.*;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.utils.Range;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

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
        
        this.persistentSocketInteractionProvider.listenForRequest(
                EndPointFunction.MATCH_UPDATE_RESPONSE,
                request -> {
                    synchronized (this.updateSyncObject) {
                        @SuppressWarnings("unchecked")
                        Request<IMatch> windowRequest = (Request<IMatch>) request;
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
                        this.resultMap = matchResultsResponse.getBody().getResultsMap();

                        resultsSyncObject.notifyAll();
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
                                window
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
    public void requestToolCardUsage(IToolCard toolCard) throws IOException, NotEnoughTokensException {
    
    }
    
    @Override
    public Map.Entry<JSONSerializable, Set<Integer>> waitForChooseDiePositionFromLocation() {
        return null;
    }
    
    @Override
    public void postChosenDiePosition(Map.Entry<IDie, Integer> chosenPosition) {
    
    }

    @Override
    public Map.Entry<IEffect[], Range<Integer>> waitForChooseBetweenEffect() {
        return null;
    }
    
    @Override
    public void postChosenEffects(IEffect[] effects) {
    
    }
    
    @Override
    public IEffect waitForContinueToRepeat() {
        return null;
    }
    
    @Override
    public void postContinueToRepeatChoice(boolean continueToRepeat) {
    
    }
    
    @Override
    public IDie waitForSetShade() {
        return null;
    }
    
    @Override
    public void postSetShade(Integer shade) {
    
    }

    private final transient Object resultsSyncObject = new Object();
    private Map<IPlayer, IResult> resultMap;
    
    @Override
    public Map<IPlayer, IResult> waitForMatchToEnd() throws InterruptedException {
        //noinspection Duplicates
        synchronized (resultsSyncObject) {
            while (this.resultMap == null) {
                this.resultsSyncObject.wait();
            }

            Map<IPlayer, IResult> temp = this.resultMap;
            this.resultMap = null;
            return temp;
        }
    }
    
    @Override
    public void close(Object... args) throws IOException {
        this.persistentSocketInteractionProvider.close();
    }
}
