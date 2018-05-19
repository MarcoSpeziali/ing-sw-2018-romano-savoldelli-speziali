package it.polimi.ingsw.views;

import it.polimi.ingsw.models.DraftPool;

public abstract class DraftPoolView implements Renderable {
    protected DraftPool draftPool;

    protected DieView[] dieView;

    public DraftPoolView(DraftPool draftPool){
        this.draftPool = draftPool;
    }
}
