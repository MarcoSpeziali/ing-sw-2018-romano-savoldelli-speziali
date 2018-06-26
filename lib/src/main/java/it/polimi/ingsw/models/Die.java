package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.listeners.DieInteractionListener;
import it.polimi.ingsw.net.mocks.IDie;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Die implements IDie {

    private static final long serialVersionUID = -311416275888290395L;

    private Integer shade;
    private GlassColor color;

    private static int lastUUID;

    private static synchronized int getNextUUID() {
        return ++lastUUID;
    }

    private final int uuid;

    private transient List<DieInteractionListener> listeners = new LinkedList<>();

    /**
     * Sets up a new die
     *
     * @param shade is the shade of the die
     * @param color is the color of the die
     */
    public Die(Integer shade, GlassColor color) {
        this.color = color;
        this.shade = shade;
        this.uuid = getNextUUID();
    }

    /**
     * @return the die's shade
     */
    @Override
    public Integer getShade() {
        return this.shade;
    }

    /**
     * @param shade assign the shade to the die
     */
    public void setShade(Integer shade) {
        this.shade = shade;

        this.listeners.forEach(
                dieInteractionListener -> dieInteractionListener.onDieShadeChanged(shade)
        );
    }

    /**
     * @return the die's color
     */
    @Override
    public GlassColor getColor() {
        return this.color;
    }

    @Override
    public int getUUID() {
        return uuid;
    }

    /**
     * @param color assign the color to the die
     */
    public void setColor(GlassColor color) {
        this.color = color;
    }

    /**
     * @return the die parameters in string format
     */
    @Override
    public String toString() {
        return String.format("Die(%d, %s)", this.shade, this.color);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Die die = (Die) o;
        return uuid == die.uuid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uuid);
    }

    public void addListener(DieInteractionListener dieInteractionListener) {
        this.listeners.add(dieInteractionListener);
    }
}
