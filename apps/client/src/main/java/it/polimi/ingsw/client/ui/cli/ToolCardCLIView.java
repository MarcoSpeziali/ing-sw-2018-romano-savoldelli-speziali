package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.net.mocks.IToolCard;

public class ToolCardCLIView extends CLIView<IToolCard> {

    public ToolCardCLIView() {
    }

    @Override
    public void setModel(IToolCard iToolCard) {
        super.model = iToolCard;
    }

    @Override
    public void render() {
        System.out.println("card id: " + this.model.getCardId());
        System.out.println("titolo: " + this.model.getTitle());
        System.out.println("descrizione: " + this.model.getDescription());
        System.out.println("effetto: " + this.model.getEffect());
        System.out.println("costo " + this.model.getEffect().getCost());
    }

    @Override
    public void init() {

    }
}
