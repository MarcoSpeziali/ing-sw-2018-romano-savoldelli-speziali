package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

public interface IResult extends JSONSerializable {

    @JSONElement("match-id")
    int getMatchId();

    @JSONElement("player")
    IPlayer getPlayer();

    @JSONElement("points")
    int getPoints();
}
