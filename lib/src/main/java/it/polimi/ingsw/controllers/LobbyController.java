package it.polimi.ingsw.controllers;

import it.polimi.ingsw.controllers.proxies.ProxyUpdateInterface;
import it.polimi.ingsw.controllers.proxies.RemotelyClosable;
import it.polimi.ingsw.controllers.proxies.RemotelyInitializable;
import it.polimi.ingsw.net.mocks.ILobby;
import it.polimi.ingsw.net.mocks.IMatch;

import java.rmi.RemoteException;

public interface LobbyController extends ProxyUpdateInterface<ILobby>, RemotelyInitializable, RemotelyClosable {
    IMatch waitForMigrationRequest() throws RemoteException, InterruptedException;
}
