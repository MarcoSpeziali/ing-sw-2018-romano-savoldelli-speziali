package it.polimi.ingsw.core.locations;

import it.polimi.ingsw.models.Die;

public interface RandomPutLocation extends PutLocation {
    void putDie(Die die);
}
