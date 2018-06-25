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

    /**
     * @param obj is the die to be compared with the current instance
     * @return true if the two dice have the same shade and color
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Die)) {
            return false;
        }

        Die die = (Die) obj;

        return this.color.equals(die.color) && this.shade.equals(die.shade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.color, this.shade);
    }

    public void addListener(DieInteractionListener dieInteractionListener) {
        this.listeners.add(dieInteractionListener);
    }
}
