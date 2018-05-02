package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.locations.AlreadyPutException;
import it.polimi.ingsw.core.locations.RandomPickLocation;
import it.polimi.ingsw.core.locations.RandomPutLocation;

public class Cell implements RandomPutLocation, RandomPickLocation {

    private GlassColor color;
    private Integer shade;
    private Die die;

    public Die getDie() {
        return this.die;
    }

    public Cell(GlassColor color) {
        this.color = color;
        this.shade = 0;
    }

    public Cell(int shade) {
        this.shade = shade;
    }

    public Cell() {
        this.shade = 0;
    }

    public GlassColor getColor() {
        if (this.color != null) {
            return this.color;
        }
        else return null;
    }

    public Integer getShade() {
        return this.shade;
    }

    public boolean isOccupied() {
        return this.die != null;
    }

    @Override
    public void putDie(Die die) {
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
