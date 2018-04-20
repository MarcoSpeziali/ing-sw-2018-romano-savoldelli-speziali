package it.polimi.ingsw.core;

import java.util.Objects;

public class Die {
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

    public Die() { }

    public Die(GlassColor color, Integer shade) {
        this.color = color;
        this.shade = shade;
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

    @Override
    public int hashCode() {
        return Objects.hash(this.color, this.shade);
    }
}
