package it.polimi.ingsw.listeners;

import it.polimi.ingsw.models.Die;

import java.io.Serializable;

public interface OnDiePickedListener extends Serializable {
    void onDiePicked(Die die, Integer location);

    default void onDiePicked(Die die) {
        this.onDiePicked(die, null);
    }
}
