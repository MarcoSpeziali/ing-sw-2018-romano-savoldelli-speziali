package it.polimi.ingsw.views;

import it.polimi.ingsw.controllers.ToolCardController;
import it.polimi.ingsw.models.ToolCard;

public abstract class ToolCardView {
    protected ToolCard toolCard;
    protected ToolCardController toolCardController;
    public ToolCardView(ToolCard toolCard) {
        this.toolCard = new ToolCard(toolCard.getCardId(), toolCard.getTitle().toString(), toolCard.getDescription().toString(), toolCard.getEffect());
    }

    public void setToolCardController(ToolCardController toolCardController) {
        this.toolCardController = toolCardController;
    }
}
