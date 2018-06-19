package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

public interface IEffect extends JSONSerializable {
    void run(String cardId);

    @JSONElement("cost")
    int getCost();

    @JSONElement("description-key")
    String getDescriptionKey();
}
