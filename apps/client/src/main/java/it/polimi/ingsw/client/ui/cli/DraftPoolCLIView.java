package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.models.DraftPool;
import it.polimi.ingsw.views.DraftPoolView;
import org.fusesource.jansi.Ansi;

import java.io.IOException;

import static org.fusesource.jansi.Ansi.Color.BLACK;
import static org.fusesource.jansi.Ansi.ansi;

public class DraftPoolCLIView extends DraftPoolView implements CLIView {

    public DraftPoolCLIView() {
        this.dieView = new DieCLIView[draftPool.getNumberOfDice()];

        for (int i = 0; i < this.draftPool.getNumberOfDice(); i++) {
            dieView[i] = new DieCLIView();
            dieView[i].setDie(this.draftPool.getDice().get(i));

        }
    }

    @Override
    public void setDraftPool(DraftPool draftPool) throws IOException {
        super.setDraftPool(draftPool);
    }

    @Override
    public void render() {
        System.out.println("Numero di dadi: " + this.draftPool.getNumberOfDice() + "\n");
        for (int i = 0; i < this.draftPool.getNumberOfDice(); i++) {
            Ansi.Color color = Ansi.Color.valueOf(this.draftPool.getDice().get(i).getColor().toAnsiColor());
            System.out.print(ansi().eraseScreen().bg(color).a(" " + this.draftPool.getDice().get(i).getShade() + " ").fg(BLACK).reset());
            System.out.print(" ");
        }

    }
}
