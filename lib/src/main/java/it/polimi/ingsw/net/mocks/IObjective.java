package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

public interface IObjective extends JSONSerializable {
    @JSONElement("points-per-completion")
    int getPointsPerCompletion();
}