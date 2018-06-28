package it.polimi.ingsw.controllers.proxies.socket;

import it.polimi.ingsw.controllers.*;
import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.mocks.IMatch;
import it.polimi.ingsw.net.mocks.IWindow;
import it.polimi.ingsw.net.providers.PersistentSocketInteractionProvider;
import it.polimi.ingsw.net.requests.WindowRequest;
import it.polimi.ingsw.net.responses.MigrationResponse;
import it.polimi.ingsw.net.utils.EndPointFunction;

import java.io.IOException;
import java.rmi.RemoteException;

public class MatchSocketProxyController implements MatchController {
    
    private static final long serialVersionUID = -3114652530748406372L;
    
    private final String clientToken;
    private transient PersistentSocketInteractionProvider persistentSocketInteractionProvider;
    
    public MatchSocketProxyController(PersistentSocketInteractionProvider persistentSocketInteractionProvider, String clientToken) {
        this.clientToken = clientToken;
        this.persistentSocketInteractionProvider = persistentSocketInteractionProvider;
    }
    
    @Override
    public void init(Object... args) throws IOException {
        this.persistentSocketInteractionProvider.postResponse(new Response<>(
                new Header(this.clientToken, EndPointFunction.MATCH_MIGRATION),
                new MigrationResponse((Integer) args[0])
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
    public void respondToWindowRequest(IWindow window) throws RemoteException {
    
    }
    
    @Override
    public WindowController waitForWindowController() throws RemoteException, InterruptedException {
        return null;
    }
    
    @Override
    public ToolCardController[] waitForToolCardControllers() throws RemoteException {
        return new ToolCardController[0];
    }
    
    @Override
    public ObjectiveCardController[] waitForPublicObjectiveCardControllers() throws RemoteException {
        return new ObjectiveCardController[0];
    }
    
    @Override
    public ObjectiveCardController waitForPrivateObjectiveCardController() throws RemoteException {
        return null;
    }
    
    @Override
    public DraftPoolController waitForDraftPoolController() throws RemoteException {
        return null;
    }
    
    @Override
    public RoundTrackController waitForRoundTrackController() throws RemoteException {
        return null;
    }
    
    @Override
    public void onUpdateReceived(IMatch update) throws RemoteException {
    
    }
    
    @Override
    public IMatch waitForUpdate() throws RemoteException, InterruptedException {
        return null;
    }
    
    @Override
    public void close(Object... args) throws IOException {
        this.persistentSocketInteractionProvider.close();
    }
}
