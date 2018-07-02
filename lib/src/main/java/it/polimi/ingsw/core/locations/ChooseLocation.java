package it.polimi.ingsw.core.locations;

import it.polimi.ingsw.models.Die;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public interface ChooseLocation extends Serializable {
    List<Integer> getFullLocations();

    List<Die> getDice();

    Die getDie(Integer location);

    List<Integer> getEmptyLocations();

    default List<Integer> getAllLocations() {
        List<Integer> allLocations = new LinkedList<>(getEmptyLocations());
        allLocations.addAll(getFullLocations());

        return allLocations;
    }
}
