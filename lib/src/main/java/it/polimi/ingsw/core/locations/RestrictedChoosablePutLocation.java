package it.polimi.ingsw.core.locations;

import it.polimi.ingsw.core.Die;

import java.util.List;

public interface RestrictedChoosablePutLocation extends ChoosablePutLocation {
    List<Integer> getPossiblePositionsForDie(Die die, Boolean ignoreColor, Boolean ignoreShade, Boolean ignoreAdjacency);
}
