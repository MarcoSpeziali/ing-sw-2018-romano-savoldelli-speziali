package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONElement;

public interface IToolCard extends ICard {
    @JSONElement("id")
    String getCardId();

    @JSONElement("effect")
    IEffect getEffect();

}
