package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

// TODO: docs
public interface IPlayer extends JSONSerializable {
    @JSONElement("id")
    int getId();

    @JSONElement("username")
    String getUsername();
}
