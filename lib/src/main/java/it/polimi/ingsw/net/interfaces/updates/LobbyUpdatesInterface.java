package it.polimi.ingsw.net.interfaces.updates;

import it.polimi.ingsw.net.mocks.ILobby;

// TODO: docs
public interface LobbyUpdatesInterface extends UpdateInterface<ILobby> {
    void onTimerStarted(int duration);
    void onTimerStopped();
    void onMatchStarting(int matchId); // TODO: matchId needed?
}
