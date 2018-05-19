package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.views.CellView;
import org.fusesource.jansi.Ansi;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class CellCLIView extends CellView {

    private char shade;
    private Ansi.Color ansiColor;
    private Cell cell;

    public CellCLIView(Cell cell) {
        this.cell = cell;

        if (cell.getColor() == null) {
            this.ansiColor = Ansi.Color.WHITE;
        }
        else {
           ansiColor = Ansi.Color.valueOf(cell.getColor().toAnsiColor());
        }

        shade = cell.getShade() == 0 ? ' ' : cell.getShade().toString().charAt(0);
    }

    @Override

    public void render() {
        if(cell.isOccupied()) {
            ansiColor = Ansi.Color.valueOf(cell.getDie().getColor().toAnsiColor());
            shade = (char) (cell.getDie().getShade()+48);
        }

        System.out.print(ansi().eraseScreen().bg(ansiColor).a(" "+shade+" ").fg(BLACK).reset());

    }
}
