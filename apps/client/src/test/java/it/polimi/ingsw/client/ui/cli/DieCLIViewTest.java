package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Die;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DieCLIViewTest {

    private DieCLIView dieCLIView;
    private Die die;

    @BeforeEach
    void setUp() {
        die = new Die(GlassColor.BLUE, 5);
        this.dieCLIView = new DieCLIView(die);

    }
    @Test
    void renderTest() {
        dieCLIView.render();
    }
}