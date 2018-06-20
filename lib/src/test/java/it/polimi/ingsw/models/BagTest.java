package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.locations.EmptyLocationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

class BagTest {
    private Bag bag;

    @BeforeEach
    void setBag() {
        this.bag = new Bag(18);
    }

    @Test
    void getDieTest() {
        Assertions.assertNotNull(bag.getDice());
        Assertions.assertEquals(90, bag.getNumberOfDice());
        for (GlassColor color : GlassColor.values()) {
            Assertions.assertEquals(18, bag.getNumberPerColor(color));
        }
    }

    @Test
    void getNumberPerColorTest() {
        Assertions.assertEquals(18, bag.getNumberPerColor(GlassColor.BLUE));
        GlassColor color = bag.pickDie().getColor();
        Assertions.assertEquals(17, bag.getNumberPerColor(color));
    }

    @Test
    void pickDieTest() {

        List<Enum> colorList = Arrays.asList(GlassColor.values());
        int initialCount = this.bag.getNumberOfDice();
        for (int i = 0; i < initialCount; i++) {
            Die picked = this.bag.pickDie();
            Assertions.assertNotNull(picked);
            Assertions.assertTrue(colorList.contains(picked.getColor()));
        }
        Assertions.assertThrows(EmptyLocationException.class, () -> this.bag.pickDie());
    }

    @Test
    void getNumberOfDieTest() {
        GlassColor color = bag.pickDie().getColor();
        Assertions.assertEquals(17, bag.getNumberPerColor(color));
        bag.putDie(new Die(0, color));
        Assertions.assertEquals(18, bag.getNumberPerColor(color));
    }

    @Test
    void putDieTest() {
        int prev = bag.getNumberOfDice();
        bag.putDie(new Die(0, GlassColor.BLUE));
        Assertions.assertEquals(prev + 1, bag.getNumberOfDice());
    }

    @Test
    void getFreeSpaceTest() {
        Assertions.assertEquals(90 - bag.getNumberOfDice(), bag.getFreeSpace());
        int removeN = new Random(System.currentTimeMillis()).nextInt(90);

        for (int i = 1; i <= removeN; i++) {
            this.bag.pickDie();

            Assertions.assertEquals(90 - i, this.bag.getNumberOfDice());
            Assertions.assertEquals(90 - this.bag.getNumberOfDice(), this.bag.getFreeSpace());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 15, 2, 90, 10, 56})
    void testFreeSpace(int numberOfDiceToPick) {
        for (int i = 1; i <= numberOfDiceToPick; i++) {
            this.bag.pickDie();

            Assertions.assertEquals(90 - i, this.bag.getNumberOfDice());
            Assertions.assertEquals(90 - this.bag.getNumberOfDice(), this.bag.getFreeSpace());
        }
    }
}