package it.polimi.ingsw.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GlassColorTest {

    @Test
    void testRGBGetters() {
        Assertions.assertEquals(255, GlassColor.RED.getRed());
        Assertions.assertEquals(0, GlassColor.RED.getGreen());
        Assertions.assertEquals(0, GlassColor.RED.getBlue());

        Assertions.assertEquals(0, GlassColor.GREEN.getRed());
        Assertions.assertEquals(255, GlassColor.GREEN.getGreen());
        Assertions.assertEquals(0, GlassColor.GREEN.getBlue());

        Assertions.assertEquals(0, GlassColor.BLUE.getRed());
        Assertions.assertEquals(0, GlassColor.BLUE.getGreen());
        Assertions.assertEquals(255, GlassColor.BLUE.getBlue());

        Assertions.assertEquals(255, GlassColor.YELLOW.getRed());
        Assertions.assertEquals(255, GlassColor.YELLOW.getGreen());
        Assertions.assertEquals(0, GlassColor.YELLOW.getBlue());

        Assertions.assertEquals(128, GlassColor.PURPLE.getRed());
        Assertions.assertEquals(0, GlassColor.PURPLE.getGreen());
        Assertions.assertEquals(128, GlassColor.PURPLE.getBlue());
    }

    @Test
    void testHexGetters() {
        Assertions.assertEquals(0xff0000, GlassColor.RED.getHex());
        Assertions.assertEquals(0x00ff00, GlassColor.GREEN.getHex());
        Assertions.assertEquals(0x0000ff, GlassColor.BLUE.getHex());
        Assertions.assertEquals(0xffff00, GlassColor.YELLOW.getHex());
        Assertions.assertEquals(0x800080, GlassColor.PURPLE.getHex());
    }

    @Test
    void testFromString() {
        Assertions.assertEquals(GlassColor.RED, GlassColor.fromString("red"));
        Assertions.assertEquals(GlassColor.RED, GlassColor.fromString("RED"));

        Assertions.assertEquals(GlassColor.GREEN, GlassColor.fromString("green"));
        Assertions.assertEquals(GlassColor.GREEN, GlassColor.fromString("GREEN"));

        Assertions.assertEquals(GlassColor.BLUE, GlassColor.fromString("blue"));
        Assertions.assertEquals(GlassColor.BLUE, GlassColor.fromString("BLUE"));

        Assertions.assertEquals(GlassColor.YELLOW, GlassColor.fromString("yellow"));
        Assertions.assertEquals(GlassColor.YELLOW, GlassColor.fromString("YELLOW"));

        Assertions.assertEquals(GlassColor.PURPLE, GlassColor.fromString("purple"));
        Assertions.assertEquals(GlassColor.PURPLE, GlassColor.fromString("PURPLE"));

        Assertions.assertThrows(NullPointerException.class, () -> {
            GlassColor.fromString(null);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            GlassColor.fromString("trsdads");
        });
    }

    @Test
    void testToString() {
        Assertions.assertEquals("red", GlassColor.RED.toString());
        Assertions.assertEquals("green", GlassColor.GREEN.toString());
        Assertions.assertEquals("blue", GlassColor.BLUE.toString());
        Assertions.assertEquals("yellow", GlassColor.YELLOW.toString());
        Assertions.assertEquals("purple", GlassColor.PURPLE.toString());
    }
}