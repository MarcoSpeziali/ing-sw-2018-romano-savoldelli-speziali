package it.polimi.ingsw.core.locations;

import it.polimi.ingsw.models.Die;

import java.util.List;

public interface ChooseLocation {
    List<Integer> getLocations();
    List<Die> getDice();
}
