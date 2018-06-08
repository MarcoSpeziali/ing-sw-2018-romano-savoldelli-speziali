package it.polimi.ingsw.net.interfaces;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.mocks.ILobby;
import it.polimi.ingsw.net.requests.LobbyJoinRequest;

import java.rmi.Remote;
import java.rmi.RemoteException;

// TODO: docs
public interface LobbyInterface extends Remote {
    Response<ILobby> joinLobby(Request<LobbyJoinRequest> request) throws RemoteException;
    void registerForRMIUpdate(LobbyUpdateInterface lobbyUpdateInterface) throws RemoteException;
}
