package it.polimi.ingsw.listeners;


import java.io.Serializable;

public interface DieInteractionListener extends Serializable {
    void onDieShadeChanged(int newShade);
}
