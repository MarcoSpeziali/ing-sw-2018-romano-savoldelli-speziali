package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.core.CardVisibility;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Bag;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.DraftPool;
import it.polimi.ingsw.views.DraftPoolView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DraftPoolCLIViewTest {
    private DraftPool draftPool;
    private DraftPoolCLIView draftPoolCLIView;

    @BeforeEach
    void setUp() {
        Bag bag = new Bag(18);
        this.draftPool = new DraftPool(4, mock(DraftPoolView.class), bag);
        draftPoolCLIView = new DraftPoolCLIView(draftPool);
    }

    @Test
    void render() {
    draftPoolCLIView.render();
    }
}