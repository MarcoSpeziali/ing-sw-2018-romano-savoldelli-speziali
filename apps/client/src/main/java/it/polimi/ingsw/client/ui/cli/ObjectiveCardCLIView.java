package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.net.mocks.IObjectiveCard;

import java.io.IOException;

public class ObjectiveCardCLIView extends CLIView<IObjectiveCard> {

    public void setModel(IObjectiveCard iObjectiveCard) throws IOException {
        super.setModel(iObjectiveCard);
    }

    @Override
    public void render() {
        System.out.println("card id: " + this.model.getId());
        System.out.println("titolo: " + this.model.getTitle());
        System.out.println("descrizione: " + this.model.getDescription());
        System.out.println("visibility: " + this.model.getVisibility().toString());
        System.out.println("obiettivo :" + this.model.getObjective());

    }

    @Override
    public void init() {

    }
}
