package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
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
        this.bag = new Bag();
    }

    @Test
    void getDieTest() {
        Assertions.assertNotNull(bag.getDice());
        Assertions.assertEquals(90, bag.getNumberOfDice());
        Assertions.assertEquals(18, bag.getNumberPerColor(GlassColor.BLUE));
        Assertions.assertEquals(18, bag.getNumberPerColor(GlassColor.GREEN));
        Assertions.assertEquals(18, bag.getNumberPerColor(GlassColor.PURPLE));
        Assertions.assertEquals(18, bag.getNumberPerColor(GlassColor.RED));
        Assertions.assertEquals(18, bag.getNumberPerColor(GlassColor.YELLOW));
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
        System.out.println(bag.pickDie().getColor());
        System.out.println(bag.pickDie().getColor());
        System.out.println(bag.pickDie().getColor());

    }

    @Test
    void getNumberOfDieTest() {
        GlassColor color = bag.pickDie().getColor();
        Assertions.assertEquals(17, bag.getNumberPerColor(color));
        bag.putDie(new Die(color, 0));
        Assertions.assertEquals(18, bag.getNumberPerColor(color));
    }

    @Test
    void putDieTest() {
        int prev = bag.getNumberOfDice();
        bag.putDie(new Die(GlassColor.BLUE, 0));
        Assertions.assertEquals(prev + 1, bag.getNumberOfDice());
    }

    @Test
    void getFreeSpaceTest() {
        Assertions.assertEquals(90-bag.getNumberOfDice(), bag.getFreeSpace());
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