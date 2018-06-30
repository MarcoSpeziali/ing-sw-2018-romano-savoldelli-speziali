package it.polimi.ingsw.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GlassColorTest {

    @Test
    void testRGBGetters() {
        Assertions.assertEquals(231, GlassColor.RED.getRed());
        Assertions.assertEquals(76, GlassColor.RED.getGreen());
        Assertions.assertEquals(60, GlassColor.RED.getBlue());

        Assertions.assertEquals(241, GlassColor.YELLOW.getRed());
        Assertions.assertEquals(196, GlassColor.YELLOW.getGreen());
        Assertions.assertEquals(15, GlassColor.YELLOW.getBlue());

        Assertions.assertEquals(46, GlassColor.GREEN.getRed());
        Assertions.assertEquals(204, GlassColor.GREEN.getGreen());
        Assertions.assertEquals(113, GlassColor.GREEN.getBlue());

        Assertions.assertEquals(52, GlassColor.BLUE.getRed());
        Assertions.assertEquals(152, GlassColor.BLUE.getGreen());
        Assertions.assertEquals(219, GlassColor.BLUE.getBlue());

        Assertions.assertEquals(155, GlassColor.PURPLE.getRed());
        Assertions.assertEquals(89, GlassColor.PURPLE.getGreen());
        Assertions.assertEquals(182, GlassColor.PURPLE.getBlue());
    }

    @Test
    void testHexGetters() {
        Assertions.assertEquals(0xe74c3c, GlassColor.RED.getHex());
        Assertions.assertEquals(0xf1c40f, GlassColor.YELLOW.getHex());
        Assertions.assertEquals(0x2ecc71, GlassColor.GREEN.getHex());
        Assertions.assertEquals(0x3498db, GlassColor.BLUE.getHex());
        Assertions.assertEquals(0x9b59b6, GlassColor.PURPLE.getHex());
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