package it.polimi.ingsw.core;

import it.polimi.ingsw.core.locations.ChooseLocation;
import it.polimi.ingsw.core.locations.PutLocation;

public interface UserInteractionProvider {
    Die chooseDie(ChooseLocation location, GlassColor glassColor, Integer shade);
    Integer choosePosition(ChooseLocation location);
    Integer choosePosition(ChooseLocation location, Die die);
    Die pickDie(Die die, GlassColor glassColor, Integer shade);
    Integer chooseShade(Die die);
    void putDie(PutLocation location, Die die, boolean random, boolean ignoreColor, boolean ignoreShade, boolean ignoreAdjacency);
}
