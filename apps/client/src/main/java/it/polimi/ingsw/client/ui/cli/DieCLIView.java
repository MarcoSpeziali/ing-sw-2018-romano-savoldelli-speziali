package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.net.mocks.IDie;
import org.fusesource.jansi.Ansi;

import java.io.IOException;
import java.util.Scanner;

import static org.fusesource.jansi.Ansi.Color.WHITE;
import static org.fusesource.jansi.Ansi.ansi;

public class DieCLIView extends CLIView<IDie> {

    private Scanner scanner = new Scanner(System.in);


    public void setModel(IDie iDie) throws IOException {
        super.setModel(iDie);
    }


    @Override
    public void render() {

        Ansi.Color color = Ansi.Color.valueOf(this.model.getColor().name());
        System.out.print(ansi().eraseScreen().bg(color).a(" " + this.model.getShade() + " ").fg(WHITE).reset());
    }

    @Override
    public void init() {

    }
}