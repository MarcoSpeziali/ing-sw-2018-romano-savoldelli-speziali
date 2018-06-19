package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

public interface IResult extends JSONSerializable {

    @JSONElement("player")
    IPlayer getPlayer();

    @JSONElement("match")
    IMatch getMatch();

    @JSONElement("points")
    int getPoints();
}
