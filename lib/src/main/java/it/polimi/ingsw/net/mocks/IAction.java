package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

public interface IAction extends JSONSerializable {
    @JSONElement("id")
    String getActionId();

    @JSONElement("name")
    String getActionName();

    @JSONElement("description")
    String getActionDescription();
}
