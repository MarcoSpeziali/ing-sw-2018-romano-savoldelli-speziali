package it.polimi.ingsw.controllers;

import it.polimi.ingsw.controllers.proxies.ProxyUpdateInterface;
import it.polimi.ingsw.net.mocks.ILobby;
import it.polimi.ingsw.net.mocks.IMatch;

import java.io.IOException;
import java.rmi.RemoteException;

public interface LobbyController extends ProxyUpdateInterface<ILobby> {
    void init() throws IOException;

    IMatch waitForMigrationRequest() throws RemoteException, InterruptedException;

    void leave() throws IOException;
}
