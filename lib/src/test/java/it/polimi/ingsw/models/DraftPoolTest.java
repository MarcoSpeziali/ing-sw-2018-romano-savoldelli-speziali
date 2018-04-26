package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DraftPoolTest {
    private DraftPool draftPool;
    private Die die;

    @BeforeEach
    void setUp() {
        draftPool = new DraftPool(4, new Bag());
        die = new Die(GlassColor.BLUE, 0);
    }

    @Test
    void pickDieTest() {
        System.out.println(draftPool.getDice());
        int prevSize = draftPool.getNumberOfDice();
        Assertions.assertEquals(die, draftPool.pickDie(die));
        Assertions.assertEquals(prevSize - 1, draftPool.getNumberOfDice());
        System.out.println(draftPool.getDice());
    }

    @Test
    void pickDie1Test() {
        System.out.println(draftPool.getDice());
        int prevSize = draftPool.getNumberOfDice();
        draftPool.pickDie(1);
        Assertions.assertEquals(prevSize - 1, draftPool.getNumberOfDice());
        System.out.println(draftPool.getDice());
        Assertions.assertThrows(IndexOutOfBoundsException.class, ()->draftPool.pickDie(-1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, ()->draftPool.pickDie(100));
    }

    @Test
    void getLocationsTest() {
        Assertions.assertEquals(draftPool.getNumberOfDice(), draftPool.getLocations().size());
    }

    @Test
    void getDiceTest() {
        Assertions.assertNotNull(draftPool.getDice());
    }

    @Test
    void getNumberOfDiceTest() {
        Assertions.assertEquals(draftPool.getNumberOfDice(), draftPool.getDice().size());
    }
}