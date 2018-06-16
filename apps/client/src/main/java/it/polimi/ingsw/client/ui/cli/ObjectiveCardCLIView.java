package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.models.ObjectiveCard;
import it.polimi.ingsw.views.ObjectiveCardView;

public class ObjectiveCardCLIView extends ObjectiveCardView implements CLIView {

    private ObjectiveCard objectiveCard;


    public ObjectiveCardCLIView(ObjectiveCard objectiveCard) {
        this.objectiveCard = objectiveCard;
    }

    @Override
    public void render() {
        System.out.println("card id: " + this.objectiveCard.getCardId());
        System.out.println("titolo: " + this.objectiveCard.getTitle().toString());
        System.out.println("descrizione: " + this.objectiveCard.getDescription().toString());
        System.out.println("visibility: " + this.objectiveCard.getVisibility().toString());
        System.out.println("obiettivo :" + this.objectiveCard.getObjective());

    }
}
