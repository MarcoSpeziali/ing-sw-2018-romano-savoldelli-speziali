package it.polimi.ingsw.controllers;

import it.polimi.ingsw.controllers.proxies.ProxyUpdateInterface;
import it.polimi.ingsw.controllers.proxies.RemotelyClosable;
import it.polimi.ingsw.controllers.proxies.RemotelyInitializable;
import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.net.mocks.IMatch;
import it.polimi.ingsw.net.mocks.IPlayer;

import java.util.Map;

public interface MatchController extends ProxyUpdateInterface<IMatch>, RemotelyInitializable, RemotelyClosable {

    // first thing is to choose the window to play with
    Window[] waitForWindowRequest();
    void respondToWindowRequest(Window window);

    // then the opponents' Window are sent
    Map<IPlayer, Window> waitForOpponentsWindowsUpdate();
}
