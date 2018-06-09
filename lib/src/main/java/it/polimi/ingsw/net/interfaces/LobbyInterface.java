package it.polimi.ingsw.net.interfaces;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.updates.LobbyUpdatesInterface;
import it.polimi.ingsw.net.mocks.ILobby;
import it.polimi.ingsw.net.requests.LobbyJoinRequest;
import it.polimi.ingsw.net.utils.EndPointFunction;

import java.rmi.Remote;
import java.rmi.RemoteException;

// TODO: docs
public interface LobbyInterface extends Remote {
    @RespondsTo(EndPointFunction.LOBBY_JOIN_REQUEST)
    Response<ILobby> joinLobby(Request<LobbyJoinRequest> request) throws RemoteException;

    @RespondsTo(EndPointFunction.LOBBY_REGISTER_FOR_UPDATES)
    void registerForRMIUpdate(LobbyUpdatesInterface lobbyUpdateInterface) throws RemoteException;
}