package it.polimi.ingsw.core.locations;

import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.GlassColor;

import java.util.Set;

public interface PickLocation {
    Set<Die> getDice(GlassColor glassColor, Integer shade);
    Set<Die> getDice(int quantity, GlassColor glassColor, Integer shade);
}