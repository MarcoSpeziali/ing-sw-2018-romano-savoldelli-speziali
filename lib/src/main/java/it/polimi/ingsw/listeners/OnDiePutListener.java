package it.polimi.ingsw.listeners;

import it.polimi.ingsw.models.Die;

import java.io.Serializable;

public interface OnDiePutListener extends Serializable {
    void onDiePut(Die die, Integer location);

    default void onDiePut(Die die) {
        this.onDiePut(die, null);
    }
}
