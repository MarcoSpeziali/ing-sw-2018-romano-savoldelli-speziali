package it.polimi.ingsw.controllers;

import it.polimi.ingsw.controllers.proxies.ProxyUpdateInterface;
import it.polimi.ingsw.controllers.proxies.RemotelyClosable;
import it.polimi.ingsw.controllers.proxies.RemotelyInitializable;
import it.polimi.ingsw.net.mocks.IMatch;
import it.polimi.ingsw.net.mocks.IObjectiveCard;
import it.polimi.ingsw.net.mocks.IWindow;

import java.rmi.RemoteException;

public interface MatchController extends ProxyUpdateInterface<IMatch>, RemotelyInitializable, RemotelyClosable {
    
    void postWindowsToChoose(IWindow[] windows) throws RemoteException;
    
    IWindow[] waitForWindowRequest() throws RemoteException;
    
    void respondToWindowRequest(IWindow window) throws RemoteException;
    
    void postWindowController(WindowController windowController) throws RemoteException;
    
    WindowController waitForWindowController() throws RemoteException;
    
    void postToolCardControllers(ToolCardController[] toolCardControllers) throws RemoteException;
    
    ToolCardController[] waitForToolCardControllers() throws RemoteException;
    
    void postPublicObjectiveCards(IObjectiveCard[] objectiveCards) throws RemoteException;
    
    ObjectiveCardController[] waitForPublicObjectiveCardControllers() throws RemoteException;
    
    void postPrivateObjectiveCard(IObjectiveCard objectiveCard) throws RemoteException;
    
    ObjectiveCardController waitForPrivateObjectiveCardController() throws RemoteException;
    
    void postDraftPoolController(DraftPoolController draftPoolController) throws RemoteException;
    
    DraftPoolController waitForDraftPoolController() throws RemoteException;
    
    void postRoundTrackController(RoundTrackController roundTrackController) throws RemoteException;
    
    RoundTrackController waitForRoundTrackController() throws RemoteException;
}
