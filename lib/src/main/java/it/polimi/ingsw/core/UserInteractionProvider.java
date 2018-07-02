package it.polimi.ingsw.core;

import it.polimi.ingsw.core.locations.ChooseLocation;
import it.polimi.ingsw.core.locations.RestrictedChoosablePutLocation;
import it.polimi.ingsw.models.Die;

import java.io.Serializable;

public interface UserInteractionProvider extends Serializable {

    default Die chooseDie(ChooseLocation location, GlassColor glassColor, Integer shade) {
        Integer position = choosePosition(location, glassColor, shade);
        return location.getDie(position);
    }

    Integer choosePosition(ChooseLocation location, GlassColor color, Integer shade);

    Integer choosePositionForDie(RestrictedChoosablePutLocation location, Die die, Boolean ignoreColor, Boolean ignoreShade, Boolean ignoreAdjacency);

    Integer chooseShade(Die die);
}
