package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.views.DieView;
import org.fusesource.jansi.Ansi;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class DieCLIView extends DieView {

    private Die die;


    public DieCLIView(Die die) {

        this.die= die;
    }


    public void render() {

        Ansi.Color color = Ansi.Color.valueOf(this.die.getColor().name());
        System.out.print(ansi().eraseScreen().bg(color).a(" " + this.die.getShade() + " ").fg(WHITE).reset());
    }
}