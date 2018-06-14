package it.polimi.ingsw.views;

import it.polimi.ingsw.controllers.WindowController;
import it.polimi.ingsw.models.Window;

public abstract class WindowView {
    protected Window window;
    protected WindowController windowController;

    public void setWindow(Window window) {
        this.window = window;
    }

    public void setWindowController(WindowController windowController) {
        this.windowController = windowController;
    }

    public WindowView(Window window) {
        this.window = window;
    }
}
