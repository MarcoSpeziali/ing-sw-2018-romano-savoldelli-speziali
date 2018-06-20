package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.models.Bag;
import it.polimi.ingsw.models.DraftPool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class DraftPoolCLIViewTest {
    private DraftPool draftPool;
    private DraftPoolCLIView draftPoolCLIView;

    @BeforeEach
    void setUp() throws IOException {
        Bag bag = new Bag(18);
        this.draftPool = new DraftPool();
        draftPoolCLIView = new DraftPoolCLIView();
        draftPoolCLIView.setDraftPool(draftPool);
    }

    @Test
    void render() {
        draftPoolCLIView.render();
    }
}