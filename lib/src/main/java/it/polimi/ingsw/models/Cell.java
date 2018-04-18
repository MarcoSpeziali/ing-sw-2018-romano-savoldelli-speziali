package it.polimi.ingsw.models;

import it.polimi.ingsw.core.*;

public class Cell {

    private GlassColor cellColor;
    private int shade;
    private Die die = null;

    public Cell(GlassColor cellColor, int shade) {
        this.cellColor = cellColor;
        this.shade = shade;
    }

    public boolean isOccupied() {
        return this.die != null;
    }

    public void putDie(Die die) {
        this.die = die;
    }

}
