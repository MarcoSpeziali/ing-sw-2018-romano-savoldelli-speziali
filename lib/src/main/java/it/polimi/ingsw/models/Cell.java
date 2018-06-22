package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.locations.FullLocationException;
import it.polimi.ingsw.core.locations.RandomPickLocation;
import it.polimi.ingsw.core.locations.RandomPutLocation;
import it.polimi.ingsw.listeners.OnDiePickedListener;
import it.polimi.ingsw.listeners.OnDiePutListener;
import it.polimi.ingsw.net.mocks.ICell;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

import java.util.LinkedList;
import java.util.List;

public class Cell implements RandomPutLocation, RandomPickLocation, ICell {

    private static final long serialVersionUID = 379193806872969294L;

    @JSONElement("color")
    private GlassColor color;

    @JSONElement("shade")
    private Integer shade;

    @JSONElement("die")
    private Die die;

    private transient List<OnDiePutListener> onDiePutListeners = new LinkedList<>();
    private transient List<OnDiePickedListener> onDiePickedListeners = new LinkedList<>();

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

    @JSONDesignatedConstructor
    Cell(@JSONElement("shade") Integer shade, @JSONElement("color") GlassColor color, @JSONElement("die") Die die) {
        this.shade = shade;
        this.color = color;
        this.die = die;
    }

    /**
     * @return an instance of {@link Die} of the cell, if present.
     */
    @Override
    public Die getDie() {
        return this.die;
    }

    /**
     * @return the color of the cell, null otherwise.
     */
    @Override
    public GlassColor getColor() {
        return this.color;
    }

    /**
     * @return the shade of the cell.
     */
    @Override
    public Integer getShade() {
        return this.shade;
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
            throw new FullLocationException(this);
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
