package it.polimi.ingsw.models;

import it.polimi.ingsw.core.*;

public class Cell {

    private GlassColor cellColor;
    private int shade;
    private Die die = null;

    public GlassColor getCellColor() {
        return this.cellColor;
    }

    public int getShade() {
        return this.shade;
    }

    public Cell(GlassColor cellColor, int shade) {
        this.cellColor = cellColor;
        this.shade = shade;
    }

    public boolean isOccupied() {
        return this.die != null;
    }

}
