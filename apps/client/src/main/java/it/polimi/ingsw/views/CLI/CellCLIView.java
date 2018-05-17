package it.polimi.ingsw.views.CLI;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.views.CellView;
import org.fusesource.jansi.Ansi;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class CellCLIView extends CellView{

    private char shade;
    private Ansi.Color ansiColor;

    public CellCLIView() {
        this.ansiColor = Ansi.Color.WHITE;
        this.shade = ' ';

    }

    public CellCLIView(GlassColor glassColor) {

        this.ansiColor = Ansi.Color.valueOf(glassColor.name());
        this.shade = ' ';
    }

    public CellCLIView(int shade) {

        this.ansiColor = Ansi.Color.WHITE;
        this.shade = (char) (shade + 48);
    }

    @Override

    public void render() {

        System.out.print(ansi().eraseScreen().bg(ansiColor).a(" "+shade+" ").fg(BLACK).reset());

    }
}
