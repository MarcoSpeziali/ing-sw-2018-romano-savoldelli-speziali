package it.polimi.ingsw.core.locations;

import it.polimi.ingsw.models.Die;

public interface ChoosablePutLocation extends ChooseLocation, PutLocation {
    void putDie(Die die, Integer location);
}
