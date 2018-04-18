package it.polimi.ingsw.core;

public enum GlassColor {
    RED(0xff0000),
    YELLOW(0xffff00),
    GREEN(0x00ff00),
    BLUE(0x0000ff),
    PURPLE(0x800080);

    private final int red;
    private final int green;
    private final int blue;

    public int getRed() {
        return this.red;
    }

    public int getGreen() {
        return this.green;
    }

    public int getBlue() {
        return this.blue;
    }

    GlassColor(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    GlassColor(int hex) {
        this(
            (hex & 0xFF0000) >> 16,
            (hex & 0xFF00) >> 8,
            (hex & 0xFF)
        );
    }

    public static GlassColor fromString(String rep) {
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
                throw new IllegalArgumentException();
        }
    }
}
