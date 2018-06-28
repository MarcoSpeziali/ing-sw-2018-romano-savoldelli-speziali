package it.polimi.ingsw.client.ui;

import java.io.IOException;

public abstract class View<T> {

    protected T model;

    public void setModel(T model) throws IOException {
        this.model = model;
        this.init();
    }

    public abstract void init();
}
