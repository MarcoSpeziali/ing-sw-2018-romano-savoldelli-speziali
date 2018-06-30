package it.polimi.ingsw.controllers.proxies.socket;

import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.controllers.NotEnoughTokensException;
import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.mocks.*;
import it.polimi.ingsw.net.providers.PersistentSocketInteractionProvider;
import it.polimi.ingsw.net.requests.MoveRequest;
import it.polimi.ingsw.net.requests.WindowRequest;
import it.polimi.ingsw.net.responses.MigrationResponse;
import it.polimi.ingsw.net.responses.MoveResponse;
import it.polimi.ingsw.net.responses.WindowResponse;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.utils.Range;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

import java.io.IOException;
import java.rmi.RemoteException;
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
    }
    
    private final transient Object windowsToChooseSyncObject = new Object();
    private IWindow[] windowsToChoose;
    
    @Override
    public IWindow[] waitForWindowRequest() throws RemoteException, InterruptedException {
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
    
    @Override
    public void waitForTurnToBegin() throws IOException {
    
    }
    
    @Override
    public void endTurn() throws IOException {
    
    }
    
    @Override
    public void waitForTurnToEnd() throws IOException {
    
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

    public Map.Entry<IEffect[], Range<Integer>> waitForChooseBetweenEffect(IEffect[] availableEffects, Range<Integer> chooseBetween) {
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
    
    
    @Override
    public void close(Object... args) throws IOException {
        this.persistentSocketInteractionProvider.close();
    }
    
    @Override
    public IMatch waitForUpdate() throws RemoteException, InterruptedException {
        return null;
    }
}
