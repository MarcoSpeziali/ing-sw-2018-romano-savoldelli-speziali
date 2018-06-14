package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.controllers.CellController;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.views.CellView;
import javafx.scene.Parent;
import org.fusesource.jansi.Ansi;

import java.util.Scanner;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class CellCLIView extends CellView implements CLIView {

    private char shade;
    private Ansi.Color ansiColor;

    public CellCLIView(Cell cell) {
        super(cell);
        this.setCell(cell);
    }

    @Override
    public void render() {
        if(cell.isOccupied()) {
            ansiColor = Ansi.Color.valueOf(cell.getDie().getColor().toAnsiColor());
            shade = (char) (cell.getDie().getShade()+48);
        }

        System.out.print(ansi().eraseScreen().bg(ansiColor).a(" "+shade+" ").fg(BLACK).reset());

    }

    @Override
    public void setCell(Cell cell) {
        super.setCell(cell);

        if (cell.getColor() == null) {
            this.ansiColor = Ansi.Color.WHITE;
        }
        else {
            ansiColor = Ansi.Color.valueOf(cell.getColor().toAnsiColor());
        }

        shade = cell.getShade() == 0 ? ' ' : cell.getShade().toString().charAt(0);
    }
}

