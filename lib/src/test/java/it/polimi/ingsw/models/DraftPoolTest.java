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
        draftPool = new DraftPool((byte) 5);
        die = new Die(0, GlassColor.BLUE);
    }

    @Test
    void pickDieTest() {
        draftPool.putDie(die);
        Assertions.assertEquals(die, draftPool.pickDie(die));
        Assertions.assertNull(draftPool.pickDie(new Die(9, GlassColor.GREEN)));
    }

    @Test
    void pickDie1Test() {
        draftPool.putDie(die);
        Assertions.assertEquals(die, draftPool.pickDie(0));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> draftPool.pickDie(0));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> draftPool.pickDie(100));
    }

    @Test
    void getLocationsTest() {
        Assertions.assertEquals(draftPool.getNumberOfDice(), draftPool.getFullLocations().size());
    }

    @Test
    void getDiceTest() {
        Assertions.assertNotNull(draftPool.getDice());
    }

    @Test
    void getNumberOfDiceTest() {
        Assertions.assertEquals(0, draftPool.getNumberOfDice());
    }

    @Test
    void getFreeSpaceTest() {
        Assertions.assertEquals(5 * 2 + 1, draftPool.getFreeSpace());
    }

    @Test
    void putDieTest() {
        this.draftPool.putDie(die);
        Assertions.assertEquals(die, this.draftPool.pickDie(die));
    }
}