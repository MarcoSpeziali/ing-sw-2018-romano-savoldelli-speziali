package it.polimi.ingsw.models;

import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.locations.AlreadyPutException;
import it.polimi.ingsw.core.locations.RandomPickLocation;
import it.polimi.ingsw.core.locations.RandomPutLocation;

public class Cell implements RandomPutLocation, RandomPickLocation {

    private GlassColor cellColor;
    private Integer shade;
    private Die die;

    public Cell(GlassColor cellColor) {
        this.cellColor = cellColor;
        this.shade = 0;
    }

    public Cell(int shade) {
        this.shade = shade;
    }

    public Cell() {
        this.shade = 0;
    }

    public GlassColor getCellColor() {
        if (this.cellColor != null) {
            return this.cellColor;
        }
        else return null;
    }

    public Integer getShade() {
        if (this.shade != null) {
            return this.shade;
        }
        else return null;
    }

    public boolean isOccupied() {
        return this.die != null;
    }

    @Override
    public void putDie(Die die) throws AlreadyPutException {
        if (this.die == null) this.die = die;
        else {
            throw new AlreadyPutException("A die has already been put on this cell!");
        }
    }

    @Override
    public int getFreeSpace() {
        return this.die == null ? 1 : 0;
    }

    @Override
    public Die pickDie() {
        if (this.die != null) {
            Die picked = die;
            die = null;
            return picked;
        }
        else return null;
    }

    @Override
    public int getNumberOfDice() {
        return this.die != null ? 1 : 0;
    }
}
