package it.polimi.ingsw.core.locations;

import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.GlassColor;

import java.util.List;

public interface PickLocation {
    List<Die> getDice(GlassColor glassColor, Integer shade);
    List<Die> getDice(int quantity, GlassColor glassColor, Integer shade);
}