package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class EffectMock implements IEffect {
    
    private static final long serialVersionUID = 1692089287657072356L;
    private final int cost;
    private final String descriptionKey;
    
    public EffectMock(IEffect iEffect) {
        this(iEffect.getCost(), iEffect.getDescriptionKey());
    }
    
    @JSONDesignatedConstructor
    public EffectMock(
       @JSONElement("cost") int cost,
       @JSONElement("description-key") String descriptionKey
    ) {
        this.cost = cost;
        this.descriptionKey = descriptionKey;
    }
    
    @Override
    @JSONElement("cost")
    public int getCost() {
        return cost;
    }

    @Override
    @JSONElement("description-key")
    public String getDescriptionKey() {
        return descriptionKey;
    }
}
