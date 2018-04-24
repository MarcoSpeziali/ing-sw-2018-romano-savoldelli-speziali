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
}