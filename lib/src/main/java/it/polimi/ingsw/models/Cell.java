package it.polimi.ingsw.models;

import it.polimi.ingsw.core.*;
import it.polimi.ingsw.core.locations.AlreadyPutException;

public class Cell {

    private GlassColor cellColor;
    private Integer shade;
    private Die die = null;

    public Cell(GlassColor cellColor) {
        this.cellColor = cellColor;
    }

    public Cell(Integer shade) {
        this.shade = shade;
    }

    public Cell() {
    }

    public GlassColor getCellColor() throws NullPointerException {
        if (this.cellColor != null) {
            return this.cellColor;
        }
        else throw new NullPointerException("This cell has no color!");
    }

    public int getShade() throws NullPointerException {
        if (this.shade != null) {
            return this.shade;
        }
        else throw new NullPointerException("This cell has no shade!");
    }

    public boolean isOccupied() {
        return this.die != null;
    }

    public void putDie(Die die) throws AlreadyPutException {
        if (this.die == null) this.die = die;
        else {
            throw new AlreadyPutException("A die has already been put on this cell!");
        }
    }

    public Die pickDie() throws NullPointerException {
        if (this.die != null) {
            Die picked = die;
            die = null;
            return picked;
        }
        else {
            throw new NullPointerException("No die has been found to be picked!");
        }
    }
}
