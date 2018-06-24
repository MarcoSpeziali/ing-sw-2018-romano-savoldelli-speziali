package it.polimi.ingsw.controllers;

import it.polimi.ingsw.controllers.proxies.ProxyUpdateInterface;
import it.polimi.ingsw.controllers.proxies.RemotelyClosable;
import it.polimi.ingsw.controllers.proxies.RemotelyInitializable;
import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.net.mocks.ILivePlayer;
import it.polimi.ingsw.net.mocks.IMatch;
import it.polimi.ingsw.net.mocks.IWindow;

import java.rmi.RemoteException;
import java.util.Map;

public interface MatchController extends ProxyUpdateInterface<IMatch>, RemotelyInitializable, RemotelyClosable {

    // first thing is to choose the window to play with
    IWindow[] waitForWindowRequest() throws RemoteException;

    WindowController respondToWindowRequest(IWindow window) throws RemoteException;

    ToolCardController[] waitForToolCards() throws RemoteException;

    ObjectiveCardController[] waitForPublicObjectiveCards() throws RemoteException;

    ObjectiveCardController waitForPrivateObjectiveCard() throws RemoteException;

    // then the opponents' Window are sent // FIXME: corrected Window with IWindow
    Map<ILivePlayer, IWindow> waitForOpponentsWindowsUpdate() throws RemoteException;
}
