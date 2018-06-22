package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

public interface IMatch extends JSONSerializable {

    @JSONElement("id")
    int getId();

    @JSONElement("starting-time")
    long getStartingTime();

    @JSONElement("ending-time")
    long getEndingTime();

    @JSONElement("lobby")
    ILobby getLobby();

    @JSONElement("players")
    ILivePlayer[] getPlayers();

    
}
