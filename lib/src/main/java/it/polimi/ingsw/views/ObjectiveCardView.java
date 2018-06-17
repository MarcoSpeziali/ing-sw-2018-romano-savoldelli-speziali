package it.polimi.ingsw.views;

import it.polimi.ingsw.controllers.ObjectiveCardController;
import it.polimi.ingsw.models.ObjectiveCard;

public abstract class ObjectiveCardView {
    protected ObjectiveCard objectiveCard;
    protected ObjectiveCardController objectiveCardController;
    public ObjectiveCardView(ObjectiveCard objectiveCard){
        this.objectiveCard = new ObjectiveCard(objectiveCard.getCardId(), objectiveCard.getVisibility(),
                objectiveCard.getTitle().toString(), objectiveCard.getDescription().toString(), objectiveCard.getObjective());
    }
    public void setObjectiveCardController(ObjectiveCardController objectiveCardController){
        this.objectiveCardController = objectiveCardController;
    }
}
