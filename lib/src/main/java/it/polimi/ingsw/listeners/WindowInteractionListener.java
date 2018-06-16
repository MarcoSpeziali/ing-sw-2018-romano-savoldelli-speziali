package it.polimi.ingsw.listeners;

import it.polimi.ingsw.models.Die;

public interface WindowInteractionListener {

    void onDiePut(Die put);

    void onDiePicked(Die picked);

    void onDiePut(Die die, int location);

    void onDiePicked(Die die, int location);

}
