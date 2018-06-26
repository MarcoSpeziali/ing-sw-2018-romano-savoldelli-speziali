package it.polimi.ingsw.controllers.proxies.socket;

import it.polimi.ingsw.controllers.*;
import it.polimi.ingsw.net.mocks.IMatch;
import it.polimi.ingsw.net.mocks.IObjectiveCard;
import it.polimi.ingsw.net.mocks.IWindow;

import java.io.IOException;
import java.rmi.RemoteException;

public class MatchSocketProxyController implements MatchController {
    
    private static final long serialVersionUID = -3114652530748406372L;
    
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
}
