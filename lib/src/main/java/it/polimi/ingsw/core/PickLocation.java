package it.polimi.ingsw.core;

import java.util.Set;

public interface PickLocation {
    Set<Die> getDice(GlassColor glassColor, Integer shade);
    Set<Die> getDice(int quantity, GlassColor glassColor, Integer shade);
}