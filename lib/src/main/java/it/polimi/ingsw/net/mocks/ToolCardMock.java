package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class ToolCardMock implements IToolCard {

    private static final long serialVersionUID = 6704695589465019734L;

    private final String id;
    private final EffectMock effect;
    private final String title;
    private final String descriptionKey;
    
    public ToolCardMock(IToolCard iToolCard) {
        this(
                iToolCard.getCardId(),
                new EffectMock(iToolCard.getEffect()),
                iToolCard.getTitle(),
                iToolCard.getDescription()
        );
    }
    
    @JSONDesignatedConstructor
    public ToolCardMock(
            @JSONElement("id") String id,
            @JSONElement("effect") EffectMock effect,
            @JSONElement("title") String title,
            @JSONElement("description") String descriptionKey
    ) {
        this.id = id;
        this.effect = effect;
        this.title = title;
        this.descriptionKey = descriptionKey;
    }

    @Override
    @JSONElement("id")
    public String getCardId() {
        return this.id;
    }

    @Override
    @JSONElement("effect")
    public IEffect getEffect() {
        return this.effect;
    }

    @Override
    @JSONElement("title")
    public String getTitle() {
        return this.title;
    }

    @Override
    @JSONElement("description")
    public String getDescription() {
        return this.descriptionKey;
    }
}
