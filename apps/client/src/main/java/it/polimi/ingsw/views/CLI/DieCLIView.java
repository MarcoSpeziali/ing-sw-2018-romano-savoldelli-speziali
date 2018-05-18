package it.polimi.ingsw.views.CLI;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.views.DieView;
import org.fusesource.jansi.Ansi;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class DieCLIView extends DieView {

    private GlassColor glassColor;
    private int shade;

    public DieCLIView(GlassColor glassColor, int shade) {
        this.glassColor = glassColor;
        this.shade = shade;
        }


    public void render() {

        Ansi.Color color = Ansi.Color.valueOf(glassColor.name());
        System.out.print(ansi().eraseScreen().bg(color).a(" "+shade+" ").fg(WHITE).reset());
    }
}