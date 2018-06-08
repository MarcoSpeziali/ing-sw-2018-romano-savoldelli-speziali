package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.JSONSerializable;

import java.util.List;

// TODO: docs
public interface ILobby extends JSONSerializable {
    int getId();

    long getOpeningTime();

    long getClosingTime();

    List<IPlayer> getPlayers();
}
