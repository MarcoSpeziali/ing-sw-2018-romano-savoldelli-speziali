package it.polimi.ingsw.core.locations;

import it.polimi.ingsw.core.Die;

import java.util.Set;

public interface RestrictedChoosablePutLocation extends ChooseLocation, PutLocation {
    Set<Integer> getPossiblePositionsForDie(Die die, Boolean ignoreColor, Boolean ignoreShade, Boolean ignoreAdjacency);
}
