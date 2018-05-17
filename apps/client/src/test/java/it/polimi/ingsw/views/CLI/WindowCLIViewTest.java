package it.polimi.ingsw.views.CLI;

import it.polimi.ingsw.core.GlassColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WindowCLIViewTest {

    private WindowCLIView windowCLIView;
    private CellCLIView[][] cells ;

    @BeforeEach
    void setup () {
        cells = new CellCLIView[][]{{new CellCLIView(GlassColor.BLUE),new CellCLIView(3), new CellCLIView(4),
                new CellCLIView(GlassColor.RED)}, {new CellCLIView(6),  new CellCLIView(),
                new CellCLIView(), new CellCLIView(GlassColor.RED)}, {new CellCLIView(1),
                new CellCLIView(GlassColor.YELLOW), new CellCLIView(1), new CellCLIView(2)}};
        windowCLIView = new WindowCLIView(3, 4, 3, "test", cells );
    }

    @Test
    void renderTest() {
        windowCLIView.render();

    }

}