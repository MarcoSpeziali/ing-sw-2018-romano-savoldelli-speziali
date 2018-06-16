package it.polimi.ingsw.core;

import java.io.Serializable;
import java.util.Objects;

public enum GlassColor implements Serializable {
    RED(0xe74c3c),
    YELLOW(0xf1c40f),
    GREEN(0x2ecc71),
    BLUE(0x3498db),
    PURPLE(0x9b59b6);

    /**
     * The hex value of the color.
     */
    private final int hex;

    /**
     * @param hex the hex value of the color
     */
    GlassColor(int hex) {
        this.hex = hex;
    }

    /**
     * Converts the string representation of the a {@link GlassColor} to an
     * instance of {@link GlassColor} that can be represented by {@code rep}.
     *
     * @param rep the string representation of the a {@link GlassColor}
     * @return the instance of {@link GlassColor} that can be represented by {@code rep}
     */
    public static GlassColor fromString(String rep) {
        Objects.requireNonNull(rep);

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

    /**
     * @return the hex value of the color
     */
    public int getHex() {
        return hex;
    }

    /**
     * @return the red component of the color
     */
    public int getRed() {
        return (this.hex & 0xFF0000) >> 16;
    }

    /**
     * @return the green component of the color
     */
    public int getGreen() {
        return (this.hex & 0xFF00) >> 8;
    }

    /**
     * @return the blue component of the color
     */
    public int getBlue() {
        return (this.hex & 0xFF);
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    // TODO: create a class GlassToAnsiColor
    public String toAnsiColor() {
        return this == GlassColor.PURPLE ? "MAGENTA" : name();
    }
}
