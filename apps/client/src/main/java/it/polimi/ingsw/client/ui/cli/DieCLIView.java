package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.views.DieView;
import org.fusesource.jansi.Ansi;

import java.util.Scanner;

import static org.fusesource.jansi.Ansi.Color.WHITE;
import static org.fusesource.jansi.Ansi.ansi;

public class DieCLIView extends DieView implements CLIView {

    private Scanner scanner = new Scanner(System.in);


    public void setDieModel(Die die) {
        super.setDie(die);

        this.die.addListener(newShade -> {
            this.die.setShade(newShade);
            this.render();

            newShade = scanner.nextInt();

            if (newShade != -1) {
                // this.dieController.setDieShade(newShade);
                // TODO: 20/06/18 Correct
            }
        });
    }

    @Override
    public void render() {

        Ansi.Color color = Ansi.Color.valueOf(this.die.getColor().name());
        System.out.print(ansi().eraseScreen().bg(color).a(" " + this.die.getShade() + " ").fg(WHITE).reset());
    }
}