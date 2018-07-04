package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.net.mocks.ICell;
import it.polimi.ingsw.net.mocks.IDie;
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

        onUpdateReceived(model.getDie());
    }

    public void onUpdateReceived(IDie update) {
        if (update != null) {
            ansiColor = Ansi.Color.valueOf(update.getColor().toAnsiColor());
            shade = (char) (update.getShade() +48);
        }
    }

    @Override
    public void init() {

    }
}


