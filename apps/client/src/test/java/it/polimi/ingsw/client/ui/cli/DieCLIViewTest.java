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
        die = new Die(5, GlassColor.BLUE);
        this.dieCLIView = new DieCLIView();
        this.dieCLIView.setDieModel(die);

    }

    @Test
    void renderTest() {
        dieCLIView.render();
    }
}