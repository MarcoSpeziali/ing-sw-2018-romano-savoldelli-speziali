package it.polimi.ingsw.controllers;

import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.Window;

public class WindowController {

    private Window window;

    public WindowController(Window window) {
        this.window = window;
    }

    public void setWindowModel(Window window) {
        this.window = window;
    }

    public Die onDiePicked(Die die) {
        return this.window.pickDie(die);
    }

    public Die onDiePicked(int location) {
        return this.window.pickDie(location);
    }

    public void onDiePut(Die die, int location) {
        this.window.putDie(die, location);
    }
}
