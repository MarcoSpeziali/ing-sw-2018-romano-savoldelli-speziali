package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

class BagTest {
    private Bag bag;

    @BeforeEach
    void setBag() {
        this.bag = new Bag();
    }

    @Test
    void getDieTest() {
        Assertions.assertNotNull(bag.getDice());
        System.out.println(bag.getDice());
    }

    @Test
    void getNumberPerColorTest() {
        Assertions.assertEquals(18, bag.getNumberPerColor(GlassColor.BLUE));
        GlassColor color = bag.pickDie().getColor();
        Assertions.assertEquals(17, bag.getNumberPerColor(color));
    }

    @Test
    void pickDieTest() {
        List<Enum> list = Arrays.asList(GlassColor.values());
        Assertions.assertNotNull(bag.pickDie());
        Assertions.assertTrue(list.contains(bag.pickDie().getColor()));
    }

    @Test
    void getNumberOfDieTest() {
        Assertions.assertEquals(90, bag.getNumberOfDice());
        System.out.println(bag.getDice());
        GlassColor color = bag.pickDie().getColor();
        Assertions.assertEquals(89, bag.getNumberOfDice());
        System.out.println(bag.getDice());
        bag.putDie(new Die(color, 0));
        Assertions.assertEquals(90, bag.getNumberOfDice());
        System.out.println(bag.getDice());
    }

    @Test
    void putDieTest() {
        int prev = bag.getNumberOfDice();
        System.out.println(bag.getDice());
        bag.putDie(new Die(GlassColor.BLUE, 0));
        Assertions.assertEquals(prev + 1, bag.getNumberOfDice());
        System.out.println(bag.getDice());
    }

    @Test
    void getFreeSpaceTest() {
        Assertions.assertEquals(90-bag.getNumberOfDice(), bag.getFreeSpace());
    }
}