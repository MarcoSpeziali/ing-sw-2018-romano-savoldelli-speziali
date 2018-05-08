package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.locations.AlreadyPutException;
import it.polimi.ingsw.core.locations.RandomPickLocation;
import it.polimi.ingsw.core.locations.RandomPutLocation;

public class Cell implements RandomPutLocation, RandomPickLocation {

    private GlassColor color;
    private Integer shade;
    private Die die;

    public Cell(Integer shade, GlassColor color) {
        this.color = color;
        this.shade = shade;
    }

    public Die getDie() {
        return this.die;
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

    public boolean isBlank() {
        return this.color == null && this.shade == 0;
    }

    public boolean canFitDie(Die die, boolean ignoreColor, boolean ignoreShade) {
        return  this.isBlank() ||
                (this.color == null || ignoreColor || this.color.equals(die.getColor())) &&
                (this.shade == 0 || ignoreShade || this.shade.equals(die.getShade()));
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
