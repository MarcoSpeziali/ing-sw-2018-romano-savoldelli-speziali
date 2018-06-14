package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.models.ToolCard;
import it.polimi.ingsw.views.ToolCardView;

public class ToolCardCLIView extends ToolCardView implements CLIView {
    private ToolCard toolCard;

    public ToolCardCLIView (ToolCard toolCard){
    this.toolCard = toolCard;
}

    @Override
    public void render() {
        System.out.println("card id: " + this.toolCard.getCardId());
        System.out.println("titolo: " + this.toolCard.getTitle().toString());
        System.out.println("descrizione: " + this.toolCard.getDescription().toString());
        System.out.println("effetto: " + this.toolCard.getEffect());
        System.out.println("costo " + this.toolCard.getEffect().getCost());
    }
}
