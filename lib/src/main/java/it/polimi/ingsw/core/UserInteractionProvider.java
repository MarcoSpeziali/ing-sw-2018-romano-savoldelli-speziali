package it.polimi.ingsw.core;

import it.polimi.ingsw.core.locations.ChooseLocation;

import java.util.Set;

public interface UserInteractionProvider {
    Set<Die> chooseDice(ChooseLocation location, Integer quantity, GlassColor glassColor, Integer shade);
    Die chooseDie(ChooseLocation location, GlassColor glassColor, Integer shade);

    Set<Die> pickDice(Set<Die> dieSet, Integer quantity, GlassColor glassColor, Integer shade);
    Die pickDie(Set<Die> location, GlassColor glassColor, Integer shade);
}
