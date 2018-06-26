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

public class MatchRMIProxyController extends UnicastRemoteObject implements MatchController, HeartBeatListener {

    protected IPlayer player;

    public MatchRMIProxyController(IPlayer player) throws RemoteException {
        this.player = player;
    }
    
    @Override
    public void postWindowsToChoose(IWindow[] windows) {
    
    }
    
    @Override
    public IWindow[] waitForWindowRequest() throws RemoteException {
        return new IWindow[0];
    }
    
    @Override
    public void respondToWindowRequest(IWindow window) throws RemoteException {
    
    }
    
    @Override
    public void postWindowController(WindowController windowController) throws RemoteException {
    
    }
    
    @Override
    public WindowController waitForWindowController() throws RemoteException {
        return null;
    }
    
    @Override
    public void postToolCardControllers(ToolCardController[] toolCardControllers) throws RemoteException {
    
    }
    
    @Override
    public ToolCardController[] waitForToolCardControllers() throws RemoteException {
        return new ToolCardController[0];
    }
    
    @Override
    public void postPublicObjectiveCards(IObjectiveCard[] objectiveCards) throws RemoteException {
    
    }
    
    @Override
    public ObjectiveCardController[] waitForPublicObjectiveCardControllers() throws RemoteException {
        return new ObjectiveCardController[0];
    }
    
    @Override
    public void postPrivateObjectiveCard(IObjectiveCard objectiveCard) throws RemoteException {
    
    }
    
    @Override
    public ObjectiveCardController waitForPrivateObjectiveCardController() throws RemoteException {
        return null;
    }
    
    @Override
    public void postDraftPoolController(DraftPoolController draftPoolController) throws RemoteException {
    
    }
    
    @Override
    public DraftPoolController waitForDraftPoolController() throws RemoteException {
        return null;
    }
    
    @Override
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
    
    }
    
    @Override
    public void init(Object... args) throws IOException {
    
    }
    
    /**
     * @return false if the client wants to close the connection
     */
    @Override
    public Boolean onHeartBeat() throws RemoteException {
        return null;
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
