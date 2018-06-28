package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.models.ToolCard;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;
import org.json.JSONObject;

public class ToolCardMock implements IToolCard{

    private static final long serialVersionUID = 6704695589465019734L;

    private  String id;
    private IEffect effect;

    @JSONDesignatedConstructor
    public ToolCardMock(
            @JSONElement("id") String id,
            @JSONElement("effect") IEffect effect

    ) {
        this.id = id;
        this.effect = effect;
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
    public String getTitle() {
        throw new UnsupportedOperationException();

    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException();
    }


}
