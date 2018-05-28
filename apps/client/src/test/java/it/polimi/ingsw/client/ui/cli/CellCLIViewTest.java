package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class CellCLIViewTest {

    private CellCLIView c1;
    private CellCLIView c2;
    private CellCLIView c3;

    @BeforeEach
    void setUp() {
        this.c1 = new CellCLIView(new Cell(5, GlassColor.YELLOW));
        this.c2 = new CellCLIView(new Cell(1, null));
        this.c3 = new CellCLIView(new Cell(0, null));
    }
    @Test
    void renderTest() {
        c1.render();
        c2.render();
        c3.render();
    }
}