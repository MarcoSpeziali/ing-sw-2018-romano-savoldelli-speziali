package it.polimi.ingsw.views.CLI;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.views.CellView;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import static org.fusesource.jansi.Ansi.ansi;

public class CellCLIView extends CellView{

    public Ansi.Color getColor() {
        return ansiColor;
    }

    public void setColor(Ansi.Color color) {
        this.ansiColor = color;
    }

    public char getShade() {
        return shade;
    }

    public void setShade(char shade) {
        this.shade = shade;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    private Ansi.Color ansiColor;
    private char shade;
    private int size;

    public CellCLIView(int shade, int size) {

        this.shade = (char) (shade+48);
        this.ansiColor = Ansi.Color.WHITE;
        if (size % 2 == 0) {
            this.size = size+1;
        }
        else this.size = size;
    }

    public CellCLIView(GlassColor color, int size) {

        this.shade = ' ';
        this.ansiColor = Ansi.Color.valueOf(color.name());
        if (size % 2 == 0) {
            this.size = size+1;
        }
        else this.size = size;
    }

    public CellCLIView(int size) {

        this.shade = ' ';
        this.ansiColor = Ansi.Color.BLACK;
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
                    System.out.print(ansi().eraseScreen().bg(ansiColor).a(" ").reset());
                else if (i == 1 || i == size)
                    System.out.print(ansi().eraseScreen().bg(ansiColor).a("  ").reset());
                else if (j == 1 || j == size)
                    System.out.print(ansi().eraseScreen().bg(ansiColor).a(" ").reset()+" ");
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

