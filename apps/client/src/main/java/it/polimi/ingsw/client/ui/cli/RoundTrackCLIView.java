package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.net.mocks.IRoundTrack;
import org.fusesource.jansi.Ansi;

import java.io.IOException;

import static org.fusesource.jansi.Ansi.Color.BLACK;
import static org.fusesource.jansi.Ansi.ansi;

public class RoundTrackCLIView extends CLIView<IRoundTrack> {

    @Override
    public void setModel(IRoundTrack iRoundTrack) throws IOException {
        super.setModel(iRoundTrack);
    }

    @Override
    public void render() {
        System.out.println("Rounds: " + this.model.getNumberOfRounds() + "\n");
        for (int i = 0; i < this.model.getNumberOfRounds(); i++) {
            Ansi.Color color = Ansi.Color.valueOf(this.model.getLocationDieMap().get(i).getColor().toAnsiColor());
            System.out.print(ansi().eraseScreen().bg(color).a(" " + this.model.getLocationDieMap().get(i).getShade() + " ").fg(BLACK).reset());
            System.out.print(" ");
        }

    }

    @Override
    public void init() {

    }
}
