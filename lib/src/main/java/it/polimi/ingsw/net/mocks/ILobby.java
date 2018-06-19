package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

// TODO: docs
public interface ILobby extends JSONSerializable {

    @JSONElement("id")
    int getId();

    @JSONElement("opening-time")
    long getOpeningTime();

    @JSONElement("closing-time")
    long getClosingTime();

    @JSONElement("time-remaining")
    int getTimeRemaining();

    @JSONElement("players")
    IPlayer[] getPlayers();
}
