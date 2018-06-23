package it.polimi.ingsw.client.ui;

import java.io.IOException;

public abstract class View<T> {

    protected T controller;

    public void setController(T controller) throws IOException {
        this.controller = controller;
        this.init();
    }

    public abstract void init();
}
