package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.controllers.WindowController;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.Window;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class WindowCLIViewTest {

    private WindowCLIView windowCLIView;
    private Window window;
    private WindowController windowController;
    private Cell[][] cells = new Cell[][]{
            {
                    new Cell(0, null), new Cell(5, null), new Cell(4, null), new Cell(0, GlassColor.GREEN)
            },
            {
                    new Cell(0, null), new Cell(0, null), new Cell(2, null), new Cell(0, GlassColor.PURPLE)
            },
            {
                    new Cell(0, GlassColor.BLUE), new Cell(2, null), new Cell(0, GlassColor.RED), new Cell(0, GlassColor.YELLOW)
            }
    };

    @BeforeEach
    void setup() throws IOException {
        this.window = new Window(4, 3, 4, "Test", null, cells);
        windowCLIView = new WindowCLIView();
        windowCLIView.setWindow(window);
    }

    @Test
    void renderTest() {
        windowCLIView.render();
        window.putDie(new Die(5, GlassColor.YELLOW), 0);
        windowCLIView.render();
    }

}
