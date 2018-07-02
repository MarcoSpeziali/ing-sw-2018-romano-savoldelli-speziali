package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.net.mocks.IDraftPool;
import org.fusesource.jansi.Ansi;

import java.io.IOException;

import static org.fusesource.jansi.Ansi.Color.BLACK;
import static org.fusesource.jansi.Ansi.ansi;

public class DraftPoolCLIView extends CLIView<IDraftPool> {

    private DieCLIView[] dieCLIView;

    public DraftPoolCLIView() {
    }

    @Override
    public void setModel(IDraftPool iDraftPool) throws IOException {
        super.setModel(iDraftPool);
    }

    @Override
    public void render() {
        System.out.println("Numero di dadi: " + this.model.getMaxNumberOfDice() + "\n");
        for (int i = 0; i < this.model.getMaxNumberOfDice(); i++) {
            Ansi.Color color = Ansi.Color.valueOf(this.model.getLocationDieMap().get(i).getColor().toAnsiColor());
            System.out.print(ansi().eraseScreen().bg(color).a(" " + this.model.getLocationDieMap().get(i).getShade() + " ").fg(BLACK).reset());
            System.out.print(" ");
        }

    }

    @Override
    public void init() {

    }
}
