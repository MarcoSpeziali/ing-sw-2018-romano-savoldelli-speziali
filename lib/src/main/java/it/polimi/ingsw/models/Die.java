package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;

import java.io.Serializable;
import java.util.Objects;

public class Die implements Serializable {

    private static final long serialVersionUID = -311416275888290395L;

    private GlassColor color;

    private Integer shade;

    public Integer getShade() {
        return this.shade;
    }

    public void setShade(Integer shade) {
        this.shade = shade;
    }

    public GlassColor getColor() {
        return this.color;
    }

    public void setColor(GlassColor color) {
        this.color = color;
    }

    public Die(GlassColor color, Integer shade) {
        this.color = color;
        this.shade = shade;
    }

    @Override
    public String toString() {
        return String.format("Die(%d, %s)", this.shade, this.color);
    }

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
