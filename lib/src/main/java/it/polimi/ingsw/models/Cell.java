package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.locations.EmptyLocationException;
import it.polimi.ingsw.core.locations.FullLocationException;
import it.polimi.ingsw.core.locations.RandomPickLocation;
import it.polimi.ingsw.core.locations.RandomPutLocation;
import it.polimi.ingsw.listeners.OnDiePickedListener;
import it.polimi.ingsw.listeners.OnDiePutListener;
import it.polimi.ingsw.net.mocks.ICell;

import java.util.LinkedList;
import java.util.List;

public class Cell implements RandomPutLocation, RandomPickLocation, ICell {

    private static final long serialVersionUID = 379193806872969294L;

    private GlassColor color;
    private Integer shade;
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
            throw new EmptyLocationException(this);
        }
    }

    /**
     * @return 1 if cell is occupied, 0 otherwise.
     */
    @Override
    public int getNumberOfDice() {
        return 1 - this.getFreeSpace();
    }

    public void addOnDiePutListener(OnDiePutListener cellListener) {
        this.onDiePutListeners.add(cellListener);
    }

    public void addOnDiePickedListener(OnDiePickedListener cellListener) {
        this.onDiePickedListeners.add(cellListener);
    }
}
