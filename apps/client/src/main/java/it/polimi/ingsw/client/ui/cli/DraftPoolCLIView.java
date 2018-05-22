package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.DraftPool;
import it.polimi.ingsw.views.DieView;
import it.polimi.ingsw.views.DraftPoolView;
import org.fusesource.jansi.Ansi;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class DraftPoolCLIView extends DraftPoolView {

    public DraftPoolCLIView(DraftPool draftPool) {
        super(draftPool);
        this.dieView = new DieCLIView[draftPool.getNumberOfDice()];

        for (int i = 0; i < this.draftPool.getNumberOfDice(); i++) {
            dieView[i] = new DieCLIView(this.draftPool.getDice().get(i));

        }
    }

    @Override
    public void render() {
        System.out.println("Numero di dadi: " + this.draftPool.getNumberOfDice()+"\n");
        for (int i = 0; i <this.draftPool.getNumberOfDice() ; i++) {
            Ansi.Color color = Ansi.Color.valueOf(this.draftPool.getDice().get(i).getColor().toAnsiColor());
            System.out.print(ansi().eraseScreen().bg(color).a(" " + this.draftPool.getDice().get(i).getShade() + " ").fg(BLACK).reset());
            System.out.print(" ");
        }

    }
}
