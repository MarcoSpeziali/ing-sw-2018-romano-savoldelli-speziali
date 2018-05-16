package it.polimi.ingsw.views.CLI;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.views.CellView;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import static org.fusesource.jansi.Ansi.Color.BLACK;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.WHITE;
import static org.fusesource.jansi.Ansi.ansi;

public class CellCLIView extends CellView{

    private GlassColor color;
    private int shade;
    private int size;

    public CellCLIView(GlassColor color, int shade, int size) {
        this.color = color;
        this.shade = shade;
        if (size % 2 == 0) {
            this.size = size+1;
        }
        else this.size = size;
    }

    @Override
    public void render() {

        System.setProperty("jansi.passthrough", "true");
        AnsiConsole.systemInstall();

        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
                if ((i == 1 || i == size) && j == size)
                    System.out.print(ansi().eraseScreen().bg(BLACK).a(" ").reset() );
                else if (i == 1 || i == size)
                    System.out.print(ansi().eraseScreen().bg(BLACK).a("  ").reset() );
                else if (j == 1 || j == size)
                    System.out.print(ansi().eraseScreen().bg(BLACK).a(" ").reset()+" " );
                else if (i == size/2+1 && j == size/2+1)
                    System.out.print(shade+" ");
                else
                    System.out.print("  ");
            }
            System.out.println();
        }

        AnsiConsole.systemUninstall();
    }

}

