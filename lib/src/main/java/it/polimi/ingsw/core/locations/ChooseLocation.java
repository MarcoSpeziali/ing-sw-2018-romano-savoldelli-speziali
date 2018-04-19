package it.polimi.ingsw.core.locations;

import it.polimi.ingsw.core.Die;

import java.util.Set;

public interface ChooseLocation {
    Set<Integer> getLocations();
    Set<Die> getDice();
}
