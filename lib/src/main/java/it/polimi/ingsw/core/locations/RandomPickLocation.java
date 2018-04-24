package it.polimi.ingsw.core.locations;

import it.polimi.ingsw.models.Die;

public interface RandomPickLocation extends PickLocation {
    Die pickDie();
}
