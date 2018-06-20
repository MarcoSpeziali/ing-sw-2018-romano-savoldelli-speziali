package it.polimi.ingsw.views;

import it.polimi.ingsw.controllers.WindowController;
import it.polimi.ingsw.models.Window;

import java.io.IOException;

public abstract class WindowView {
    protected Window window;
    protected WindowController windowController;

    public WindowView() {
    }

    public void setWindow(Window window) throws IOException {
        this.window = window;
    }

    public void setWindowController(WindowController windowController) {
        this.windowController = windowController;
    }
}
