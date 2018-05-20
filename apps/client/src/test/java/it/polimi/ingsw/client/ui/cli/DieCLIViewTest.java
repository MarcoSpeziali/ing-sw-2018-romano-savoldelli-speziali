package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.core.GlassColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DieCLIViewTest {

    private DieCLIView dieCLIView;

    @BeforeEach
    void setUp() {
        this.dieCLIView = new DieCLIView(GlassColor.GREEN, 5);
    }
    @Test
    void renderTest() {
        dieCLIView.render();
    }
}