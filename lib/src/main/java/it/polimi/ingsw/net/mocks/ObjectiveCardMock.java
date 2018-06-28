package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.core.CardVisibility;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class ObjectiveCardMock implements  IObjectiveCard{

    private static final long serialVersionUID = 8882572023918644221L;

    private String id;
    private CardVisibility visibility;
    private IObjective objective;

    @JSONDesignatedConstructor
    public ObjectiveCardMock(
            @JSONElement("id") String id,
            @JSONElement("visibility") CardVisibility visibility,
            @JSONElement("objective") IObjective objective
    ) {
        this.id = id;
        this.objective = objective;
        this.visibility = visibility;
    }

    @Override
    @JSONElement("id")
    public String getId() {
        return this.id;
    }

    @Override
    @JSONElement("visibility")
    public CardVisibility getVisibility() {
        return this.visibility;
    }

    @Override
    @JSONElement("objective")
    public IObjective getObjective() {
        return this.objective;
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
