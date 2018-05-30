package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.models.Bag;
import it.polimi.ingsw.models.DraftPool;
import it.polimi.ingsw.views.DraftPoolView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;

class DraftPoolCLIViewTest {
    private DraftPool draftPool;
    private DraftPoolCLIView draftPoolCLIView;

    @BeforeEach
    void setUp() {
        Bag bag = new Bag(18);
        this.draftPool = new DraftPool();
        draftPoolCLIView = new DraftPoolCLIView(draftPool);
    }

    @Test
    void render() {
    draftPoolCLIView.render();
    }
}