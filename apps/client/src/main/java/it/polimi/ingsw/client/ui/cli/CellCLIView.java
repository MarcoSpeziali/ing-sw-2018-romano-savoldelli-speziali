package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.net.mocks.ICell;
import org.fusesource.jansi.Ansi;

import java.io.IOException;

import static org.fusesource.jansi.Ansi.Color.BLACK;
import static org.fusesource.jansi.Ansi.ansi;

public class CellCLIView extends CLIView<ICell> {

    private char shade;
    private Ansi.Color ansiColor;

    public CellCLIView() {
    }

    @Override
    public void render() {
        if (this.model.isOccupied()) {
            ansiColor = Ansi.Color.valueOf(this.model.getDie().getColor().toAnsiColor());
            shade = (char) (this.model.getDie().getShade() + 48);
        }

        System.out.print(ansi().eraseScreen().bg(ansiColor).a(" " + shade + " ").fg(BLACK).reset());

    }

    @Override
    public void setModel(ICell iCell) throws IOException {
        super.setModel(iCell);

        if (iCell.getColor() == null) {
            this.ansiColor = Ansi.Color.WHITE;
        } else {
            ansiColor = Ansi.Color.valueOf(iCell.getColor().toAnsiColor());
        }

        shade = iCell.getShade() == 0 ? ' ' : iCell.getShade().toString().charAt(0);
    }

    @Override
    public void init() {

    }
}


