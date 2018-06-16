package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.locations.AlreadyPutException;
import it.polimi.ingsw.core.locations.RandomPickLocation;
import it.polimi.ingsw.core.locations.RandomPutLocation;
import it.polimi.ingsw.listeners.OnDiePickedListener;
import it.polimi.ingsw.listeners.OnDiePutListener;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Cell implements RandomPutLocation, RandomPickLocation, Serializable {

    private static final long serialVersionUID = 379193806872969294L;

    private GlassColor color;
    private Integer shade;
    private Die die;
    private List<OnDiePutListener> onDiePutListeners = new LinkedList<>();
    private List<OnDiePickedListener> onDiePickedListeners = new LinkedList<>();

    /**
     * Sets up a new {@link Cell}.
     *
     * @param shade the shade of that cell.
     * @param color the {@link GlassColor} of that cell.
     */
    public Cell(Integer shade, GlassColor color) {
        this.color = color;
        this.shade = shade;
    }

    /**
     * @return an instance of {@link Die} of the cell, if present.
     */
    public Die getDie() {
        return this.die;
    }

    /**
     * @return the color of the cell, null otherwise.
     */
    public GlassColor getColor() {
        if (this.color != null) {
            return this.color;
        }
        else {
            return null;
        }
    }

    /**
     * @return the shade of the cell.
     */
    public Integer getShade() {
        return this.shade;
    }

    /**
     * @return true if a {@link Die} is present in the cell, false otherwise.
     */
    public boolean isOccupied() {
        return this.die != null;
    }

    /**
     * @return true if the cell has not shade nor color, false otherwise.
     */
    public boolean isBlank() {
        return this.color == null && this.shade == 0;
    }

    /**
     * Compares a specified {@link Die} with the cell.
     *
     * @param die         an instance of the {@link Die} to compare.
     * @param ignoreColor the boolean flag which avoids color control.
     * @param ignoreShade the boolean flag which avoids shade control.
     * @return true if cell matches with color, shade or is blank, false otherwise.
     */
    public boolean matchesOrBlank(Die die, boolean ignoreColor, boolean ignoreShade) {
        return this.isBlank() ||
                (this.color == null || ignoreColor || this.color.equals(die.getColor())) &&
                        (this.shade == 0 || ignoreShade || this.shade.equals(die.getShade()));
    }

    /**
     * @return 1 if cell is free, 0 otherwise.
     */
    @Override
    public int getFreeSpace() {
        return this.die == null ? 1 : 0;
    }

    /**
     * Puts the die in the specified cell.
     *
     * @param die the {@link Die} that must be put into.
     */
    @Override
    public void putDie(Die die) {
        if (this.die == null) {
            this.die = die;
        }
        else {
            throw new AlreadyPutException("A die has already been put on this cell!");
        }

        this.onDiePutListeners.forEach(dieInteractionListener ->
                dieInteractionListener.onDiePut(die));
    }

    /**
     * Picks up and removes a {@link Die} from the cell
     *
     * @return the picked {@link Die}
     */
    @Override
    public Die pickDie() {
        if (this.die != null) {
            Die picked = die;
            this.die = null;

            this.onDiePickedListeners.forEach(dieInteractionListener ->
                    dieInteractionListener.onDiePicked(picked)
            );

            return picked;
        }
        else {
            return null;
        }
    }

    /**
     * @return 1 if cell is occupied, 0 otherwise.
     */
    @Override
    public int getNumberOfDice() {
        return 1 - this.getFreeSpace();
    }

    public void addListener(OnDiePutListener cellListener) {
        this.onDiePutListeners.add(cellListener);
    }

    public void addListener(OnDiePickedListener cellListener) {
        this.onDiePickedListeners.add(cellListener);
    }
}
