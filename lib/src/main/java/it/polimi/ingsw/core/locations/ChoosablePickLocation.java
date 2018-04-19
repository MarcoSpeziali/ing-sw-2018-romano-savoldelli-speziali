package it.polimi.ingsw.core.locations;

import it.polimi.ingsw.core.Die;

public interface ChoosablePickLocation extends ChooseLocation, PickLocation {
    Die pickDie(Die die);
    Die pickDie(Integer location);
}
