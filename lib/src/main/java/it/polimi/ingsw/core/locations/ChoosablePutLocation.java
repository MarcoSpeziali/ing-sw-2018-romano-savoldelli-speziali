package it.polimi.ingsw.core.locations;

import it.polimi.ingsw.core.Die;

public interface ChoosablePutLocation extends ChooseLocation, PutLocation {
    void putDie(Die die, Integer location);
}
