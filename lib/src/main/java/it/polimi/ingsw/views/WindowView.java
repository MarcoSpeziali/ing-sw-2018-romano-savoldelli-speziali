package it.polimi.ingsw.views;

import it.polimi.ingsw.models.Window;

public abstract class WindowView implements Renderable {
    protected Window window;

    protected CellView[][] cellViews;

    public WindowView(Window window) {
        this.window = window;
    }
}
