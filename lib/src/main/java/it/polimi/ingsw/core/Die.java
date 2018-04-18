package it.polimi.ingsw.core;

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
}
