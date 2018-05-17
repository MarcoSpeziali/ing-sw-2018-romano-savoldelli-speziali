package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;

import java.io.Serializable;
import java.util.Objects;

public class Die implements Serializable {

    private static final long serialVersionUID = -311416275888290395L;

    private GlassColor color;

    private Integer shade;

    /**
     * Sets up a new die
     * @param color is the color of the die
     * @param shade is the shade of the die
     */
    public Die(GlassColor color, Integer shade) {
        this.color = color;
        this.shade = shade;
    }

    /**
     * @return the die's shade
     */
    public Integer getShade() {
        return this.shade;
    }

    /**
     * @param shade assign the shade to the die
     */
    public void setShade(Integer shade) {
        this.shade = shade;
    }

    /**
     * @return the die's color
     */
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
     * @param obj is the die to be compared
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
}
