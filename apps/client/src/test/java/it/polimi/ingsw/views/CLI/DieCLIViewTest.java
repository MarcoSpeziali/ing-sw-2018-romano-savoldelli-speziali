package it.polimi.ingsw.views.CLI;

import it.polimi.ingsw.core.GlassColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DieCLIViewTest {

    private DieCLIView dieCLIView;

    @BeforeEach
    void setUp() {
        this.dieCLIView = new DieCLIView(GlassColor.GREEN, 5, 7);
    }
    @Test
    void renderTest() {
        dieCLIView.render();
    }
}