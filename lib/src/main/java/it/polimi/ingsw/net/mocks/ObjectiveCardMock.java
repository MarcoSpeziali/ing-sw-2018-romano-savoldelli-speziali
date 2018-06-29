package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.core.CardVisibility;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class ObjectiveCardMock implements IObjectiveCard {

    private static final long serialVersionUID = 8882572023918644221L;

    private final String id;
    private final CardVisibility visibility;
    private final ObjectiveMock objective;
    private final String title;
    private final String descriptionKey;
    
    public ObjectiveCardMock(IObjectiveCard iObjectiveCard) {
        this(
                iObjectiveCard.getId(),
                iObjectiveCard.getVisibility(),
                new ObjectiveMock(iObjectiveCard.getObjective()),
                iObjectiveCard.getTitle(),
                iObjectiveCard.getDescription()
        );
    }

    @JSONDesignatedConstructor
    public ObjectiveCardMock(
            @JSONElement("id") String id,
            @JSONElement("visibility") CardVisibility visibility,
            @JSONElement("objective") ObjectiveMock objective,
            @JSONElement("title") String title,
            @JSONElement("description") String descriptionKey
    ) {
        this.id = id;
        this.objective = objective;
        this.visibility = visibility;
        this.title = title;
        this.descriptionKey = descriptionKey;
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
    @JSONElement("title")
    public String getTitle() {
        return title;
    }

    @Override
    @JSONElement("description")
    public String getDescription() {
        return descriptionKey;
    }
}
