package it.polimi.ingsw.core.locations;

import it.polimi.ingsw.core.Die;

public interface RandomPickLocation extends PickLocation {
    Die pickDie();
}
