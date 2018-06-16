package it.polimi.ingsw.net.interfaces.updates;

import it.polimi.ingsw.net.mocks.ILobby;

import java.rmi.RemoteException;

// TODO: docs
public interface LobbyUpdatesInterface extends UpdateInterface<ILobby> {
    void onTimerStarted(int duration) throws RemoteException;

    void onTimerStopped() throws RemoteException;

    void onMatchStarting(int matchId) throws RemoteException; // TODO: matchId needed?
}
