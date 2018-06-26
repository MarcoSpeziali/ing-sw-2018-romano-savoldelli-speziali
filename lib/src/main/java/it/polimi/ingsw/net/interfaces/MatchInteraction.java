package it.polimi.ingsw.net.interfaces;

import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

public interface MatchInteraction extends JSONSerializable {

    @JSONElement("match-id")
    int getMatchId();
}
