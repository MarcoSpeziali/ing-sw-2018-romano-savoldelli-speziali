package it.polimi.ingsw.controllers;

import it.polimi.ingsw.controllers.proxies.ProxyUpdateInterface;
import it.polimi.ingsw.controllers.proxies.RemotelyClosable;
import it.polimi.ingsw.controllers.proxies.RemotelyInitializable;
import it.polimi.ingsw.net.mocks.IMatch;
import it.polimi.ingsw.net.mocks.IWindow;

import java.rmi.RemoteException;

public interface MatchController extends ProxyUpdateInterface<IMatch>, RemotelyInitializable, RemotelyClosable {
    
    IWindow[] waitForWindowRequest() throws RemoteException, InterruptedException;
    
    void respondToWindowRequest(IWindow window) throws RemoteException;
    
    WindowController waitForWindowController() throws RemoteException, InterruptedException;
    
    ToolCardController[] waitForToolCardControllers() throws RemoteException;
    
    ObjectiveCardController[] waitForPublicObjectiveCardControllers() throws RemoteException;
    
    ObjectiveCardController waitForPrivateObjectiveCardController() throws RemoteException;
    
    DraftPoolController waitForDraftPoolController() throws RemoteException;
    
    RoundTrackController waitForRoundTrackController() throws RemoteException;
}
