package it.polimi.ingsw.core;

import it.polimi.ingsw.core.locations.ChooseLocation;
import it.polimi.ingsw.core.locations.RestrictedChoosablePutLocation;

public interface UserInteractionProvider {
    Die chooseDie(ChooseLocation location, GlassColor glassColor, Integer shade);
    Integer choosePosition(ChooseLocation location);
    Integer choosePosition(RestrictedChoosablePutLocation location, Die die, Boolean ignoreColor, Boolean ignoreShade, Boolean ignoreAdjacency);
    Die pickDie(Die die, GlassColor glassColor, Integer shade);
    Integer chooseShade(Die die);
}
