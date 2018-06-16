package it.polimi.ingsw.core.locations;

import it.polimi.ingsw.models.Die;

import java.io.Serializable;
import java.util.List;

public interface ChooseLocation extends Serializable {
    List<Integer> getLocations();

    List<Die> getDice();
}
