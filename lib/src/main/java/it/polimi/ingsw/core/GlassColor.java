package it.polimi.ingsw.core;

import java.io.Serializable;

public enum GlassColor implements Serializable {
    RED(0xff0000),
    YELLOW(0xffff00),
    GREEN(0x00ff00),
    BLUE(0x0000ff),
    PURPLE(0x800080);

    /**
     * The hex color.
     */
    private final int hex;

    public int getRed() {
        return (this.hex & 0xFF0000) >> 16;
    }

    public int getGreen() {
        return (this.hex & 0xFF00) >> 8;
    }

    public int getBlue() {
        return (this.hex & 0xFF);
    }

    GlassColor(int hex) {
        this.hex = hex;
    }

    public static GlassColor fromString(String rep) {
        rep = rep.trim().toLowerCase();

        switch (rep) {
            case "red":
                return GlassColor.RED;
            case "yellow":
                return GlassColor.YELLOW;
            case "green":
                return GlassColor.GREEN;
            case "blue":
                return GlassColor.BLUE;
            case "purple":
                return GlassColor.PURPLE;
            default:
                throw new IllegalArgumentException("Unrecognized color: " + rep);
        }
    }

    @Override
    public String toString() {
        return String.format("%s", this.name().toLowerCase());
    }
}
