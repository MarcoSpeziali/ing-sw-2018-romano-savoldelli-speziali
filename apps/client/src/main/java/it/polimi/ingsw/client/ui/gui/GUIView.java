package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.ui.View;


public abstract class GUIView<T> extends View<T> {
    public  void setController(T controller) {
        super.setController(controller);
    }

}
