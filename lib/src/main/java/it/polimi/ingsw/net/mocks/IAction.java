package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

public interface IAction extends JSONSerializable {
    @JSONElement("id")
    String getId();

    @JSONElement("name")
    String getName();

    @JSONElement("description")
    String getDescription();
}
