package it.polimi.ingsw.views.CLI;

import it.polimi.ingsw.core.GlassColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class CellCLIViewTest {

    private CellCLIView c1;
    private CellCLIView c2;
    private CellCLIView c3;

    @BeforeEach
    void setUp() {
        this.c1 = new CellCLIView(GlassColor.YELLOW, 5);
        this.c2 = new CellCLIView(1,5);
        this.c3 = new CellCLIView(5);
    }
    @Test
    void renderTest() {
        c1.render();
        c2.render();
        c3.render();
    }
}