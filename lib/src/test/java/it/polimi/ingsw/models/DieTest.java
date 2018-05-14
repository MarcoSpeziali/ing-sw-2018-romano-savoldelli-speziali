package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DieTest {

    public Die die;

    @BeforeEach
    void setUp() {
        this.die = new Die(GlassColor.BLUE, 4);
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

    @Test
    void equalsTest() {
        Die d1 = new Die(GlassColor.RED, 5);
        Die d2 = new Die(GlassColor.RED, 5);
        Die d3 = new Die(GlassColor.YELLOW, 5);
        Die d4 = new Die(GlassColor.RED, 4);
        Assertions.assertEquals(d1, d2);
        Assertions.assertNotEquals(d1, d3);
        Assertions.assertNotEquals(d1, d4);
        Assertions.assertEquals(d1, d2);
        Assertions.assertNotEquals(d1, new Object());

    }
}