package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        int prevSize = draftPool.getNumberOfDice();
        Assertions.assertEquals(die, draftPool.pickDie(die));
        Assertions.assertEquals(prevSize - 1, draftPool.getNumberOfDice());
    }

    @Test
    void pickDie1Test() {
        int prevSize = draftPool.getNumberOfDice();
        draftPool.pickDie(1);
        Assertions.assertEquals(prevSize - 1, draftPool.getNumberOfDice());
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