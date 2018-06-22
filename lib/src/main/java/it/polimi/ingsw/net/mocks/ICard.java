package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

public interface ICard extends JSONSerializable {
    @JSONElement("title")
    String getTitle();

    @JSONElement("description")
    String getDescription();
}
