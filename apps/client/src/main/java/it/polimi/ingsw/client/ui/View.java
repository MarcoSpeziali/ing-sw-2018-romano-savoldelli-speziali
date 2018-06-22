package it.polimi.ingsw.client.ui;

public abstract class View<T> {

    protected T controller;

    public void setController(T controller) {
        this.controller = controller;
    }
}
