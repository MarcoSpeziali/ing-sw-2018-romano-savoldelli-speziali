package it.polimi.ingsw.views.CLI;

import it.polimi.ingsw.core.GlassColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class CellCLIViewTest {

    private CellCLIView CellCLIView;

    @BeforeEach
    void setUp() {
        this.CellCLIView = new CellCLIView(GlassColor.GREEN, 5, 7);
    }
    @Test
    void renderTest() {
        CellCLIView.render();
    }
}