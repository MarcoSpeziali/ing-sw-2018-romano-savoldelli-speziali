package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.core.CardVisibility;
import it.polimi.ingsw.utils.io.json.JSONElement;

public interface IObjectiveCard extends ICard {
    @JSONElement("id")
    String getId();

    @JSONElement("visibility")
    CardVisibility getVisibility();

    @JSONElement("objective")
    IObjective getObjective();
}
