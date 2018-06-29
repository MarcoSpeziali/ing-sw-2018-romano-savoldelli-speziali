package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DieTest {

    public Die die;

    @BeforeEach
    void setUp() {
        this.die = new Die(4, GlassColor.BLUE);
    }

    @Test
    void getShadeTest() {
        Assertions.assertEquals(Integer.valueOf(4), die.getShade());
    }

    @Test
    void setShadeTest() {
        Assertions.assertNotEquals(5, die.getShade());
        die.setShade(5);
        Assertions.assertEquals(Integer.valueOf(5), die.getShade());
    }

    @Test
    void getColorTest() {
        Assertions.assertEquals(GlassColor.BLUE, die.getColor());
    }

    @Test
    void setColorTest() {
        Assertions.assertNotEquals(GlassColor.GREEN, die.getColor());
        die.setColor(GlassColor.GREEN);
        Assertions.assertEquals(GlassColor.GREEN, die.getColor());
    }

    /*
    @Test
    void equalsTest() {
        Die d1 = new Die(5, GlassColor.RED);
        Die d2 = new Die(5, GlassColor.RED);
        Die d3 = new Die(5, GlassColor.YELLOW);
        Die d4 = new Die(4, GlassColor.RED);
        Assertions.assertEquals(d1, d2);
        Assertions.assertNotEquals(d1, d3);
        Assertions.assertNotEquals(d1, d4);
        Assertions.assertEquals(d1, d2);
        Assertions.assertNotEquals(d1, new Object());
    }
    */

    @Test
    void toStringTest() {
        Assertions.assertEquals("Die(4, blue)", die.toString());
    }

    /*@Test
    void addListener() {
    }
    */
}