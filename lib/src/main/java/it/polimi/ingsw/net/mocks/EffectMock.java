package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONElement;

public class EffectMock implements IEffect {

    @Override
    @JSONElement("cost")
    public int getCost() {
        return 0;
    }

    @Override
    @JSONElement("description-key")
    public String getDescriptionKey() {
        return null;
    }
}
