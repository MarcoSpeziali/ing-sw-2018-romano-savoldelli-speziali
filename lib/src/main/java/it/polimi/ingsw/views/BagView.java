package it.polimi.ingsw.views;

import it.polimi.ingsw.models.Bag;

public abstract class BagView implements Renderable {
    protected Bag bag;

    public BagView(Bag bag) {
        this.bag = bag;
    }
}
