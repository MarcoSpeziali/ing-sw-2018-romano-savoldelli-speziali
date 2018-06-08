package it.polimi.ingsw.net.interfaces;

import it.polimi.ingsw.net.mocks.ILobby;

import java.rmi.Remote;

// TODO: docs
public interface LobbyUpdateInterface extends Remote {
    void onUpdateReceived(ILobby lobbyUpdate);
}
