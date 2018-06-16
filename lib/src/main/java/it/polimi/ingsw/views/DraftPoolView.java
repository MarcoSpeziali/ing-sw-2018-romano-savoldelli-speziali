package it.polimi.ingsw.views;

import it.polimi.ingsw.controllers.DraftPoolController;
import it.polimi.ingsw.models.DraftPool;

public abstract class DraftPoolView {
    protected DraftPool draftPool;
    protected DieView[] dieView;
    protected DraftPoolController draftPoolController;

    public DraftPoolView(DraftPool draftPool) {
        this.draftPool = draftPool;
    }

    public void setDraftPool(DraftPool draftPool) {
        this.draftPool = draftPool;
    }

    public void setDraftPoolController(DraftPoolController draftPoolController) {
        this.draftPoolController = draftPoolController;
    }
}
