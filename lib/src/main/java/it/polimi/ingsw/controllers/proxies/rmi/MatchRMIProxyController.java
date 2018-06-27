package it.polimi.ingsw.controllers.proxies.rmi;

import it.polimi.ingsw.controllers.*;
import it.polimi.ingsw.net.mocks.IMatch;
import it.polimi.ingsw.net.mocks.IObjectiveCard;
import it.polimi.ingsw.net.mocks.IPlayer;
import it.polimi.ingsw.net.mocks.IWindow;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;
import java.util.function.Consumer;

public class MatchRMIProxyController extends UnicastRemoteObject implements MatchController, HeartBeatListener {
    
    private static final long serialVersionUID = 5011425771999481032L;
    
    protected IPlayer player;
    private Boolean shouldBeKeptAlive;

    public MatchRMIProxyController(IPlayer player) throws RemoteException {
        super();
        
        this.player = player;
        this.shouldBeKeptAlive = true;
    }
    
    @Override
    public void init(Object... args) throws IOException {
        // useless in rmi
    }
    
    private final transient Object windowsToChooseSyncObject = new Object();
    private IWindow[] windowsToChoose;
    
    public void postWindowsToChoose(IWindow[] windows) {
        synchronized (this.windowsToChooseSyncObject) {
            this.windowsToChoose = windows;
    
            this.windowsToChooseSyncObject.notifyAll();
        }
    }
    
    @Override
    public IWindow[] waitForWindowRequest() throws RemoteException, InterruptedException {
        synchronized (this.windowsToChooseSyncObject) {
            while (this.windowsToChoose == null) {
                this.windowsToChooseSyncObject.wait();
            }
        
            return this.windowsToChoose;
        }
    }
    
    private transient Consumer<IWindow> windowRequestConsumer;
    
    public void setWindowRequestConsumer(Consumer<IWindow> windowRequestConsumer) {
        this.windowRequestConsumer = windowRequestConsumer;
    }
    
    @Override
    public void respondToWindowRequest(IWindow window) throws RemoteException {
        if (this.windowRequestConsumer != null) {
            this.windowRequestConsumer.accept(window);
        }
    }
    
    private final transient Object windowControllerSyncObject = new Object();
    private WindowController windowController;
    
    public void postWindowController(WindowController windowController) throws RemoteException {
        synchronized (this.windowControllerSyncObject) {
            this.windowController = windowController;
    
            this.windowControllerSyncObject.notifyAll();
        }
    }
    
    @Override
    public WindowController waitForWindowController() throws RemoteException, InterruptedException {
        synchronized (this.windowControllerSyncObject) {
            while (this.windowController == null) {
                this.windowControllerSyncObject.wait();
            }
        
            return this.windowController;
        }
    }
    
    public void postToolCardControllers(ToolCardController[] toolCardControllers) throws RemoteException {
    
    }
    
    @Override
    public ToolCardController[] waitForToolCardControllers() throws RemoteException {
        return new ToolCardController[0];
    }
    
    public void postPublicObjectiveCards(IObjectiveCard[] objectiveCards) throws RemoteException {
    
    }
    
    @Override
    public ObjectiveCardController[] waitForPublicObjectiveCardControllers() throws RemoteException {
        return new ObjectiveCardController[0];
    }
    
    public void postPrivateObjectiveCard(IObjectiveCard objectiveCard) throws RemoteException {
    
    }
    
    @Override
    public ObjectiveCardController waitForPrivateObjectiveCardController() throws RemoteException {
        return null;
    }
    
    public void postDraftPoolController(DraftPoolController draftPoolController) throws RemoteException {
    
    }
    
    @Override
    public DraftPoolController waitForDraftPoolController() throws RemoteException {
        return null;
    }
    
    public void postRoundTrackController(RoundTrackController roundTrackController) throws RemoteException {
    
    }
    
    @Override
    public RoundTrackController waitForRoundTrackController() throws RemoteException {
        return null;
    }
    
    @Override
    public IMatch waitForUpdate() throws RemoteException, InterruptedException {
        return null;
    }
    
    @Override
    public void close(Object... args) throws IOException {
        this.shouldBeKeptAlive = false;
    }

    @Override
    public Boolean onHeartBeat() throws RemoteException {
        return this.shouldBeKeptAlive;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        MatchRMIProxyController that = (MatchRMIProxyController) o;
        return Objects.equals(player, that.player);
    }
    
    @Override
    public int hashCode() {
        return this.player.hashCode();
    }
}
