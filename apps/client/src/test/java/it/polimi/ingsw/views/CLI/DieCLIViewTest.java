package it.polimi.ingsw.views.CLI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DieCLIViewTest {

    private DieCLIView dieCLIView;

    @BeforeEach
    void setUp() {
    }
    @Test
    void renderTest() {
        dieCLIView.render();
    }
}