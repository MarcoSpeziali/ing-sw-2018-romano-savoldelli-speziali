package it.polimi.ingsw.controllers;

import it.polimi.ingsw.controllers.proxies.ProxyUpdateInterface;
import it.polimi.ingsw.net.mocks.ILobby;

import java.io.IOException;

public interface LobbyController extends ProxyUpdateInterface<ILobby> {
    void init() throws IOException;

    void close() throws IOException;
}
