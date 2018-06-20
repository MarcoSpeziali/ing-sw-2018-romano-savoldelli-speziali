package it.polimi.ingsw.views;

import it.polimi.ingsw.controllers.DraftPoolController;
import it.polimi.ingsw.models.DraftPool;

import java.io.IOException;

public abstract class DraftPoolView {
    protected DraftPool draftPool;
    protected DieView[] dieView;
    protected DraftPoolController draftPoolController;

    public DraftPoolView() {
    }

    public void setDraftPool(DraftPool draftPool) throws IOException {
        this.draftPool = draftPool;
    }

    public void setDraftPoolController(DraftPoolController draftPoolController) {
        this.draftPoolController = draftPoolController;
    }
}
