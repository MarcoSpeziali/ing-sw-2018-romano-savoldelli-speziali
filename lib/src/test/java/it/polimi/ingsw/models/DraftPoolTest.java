package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.views.DraftPoolView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.AsynchronousServerSocketChannel;

import static org.mockito.Mockito.mock;

class DraftPoolTest {
    private DraftPool draftPool;
    private Die die;
    private Bag bag;

    @BeforeEach
    void setUp() {
        bag = new Bag(18);
        draftPool = new DraftPool();
        die = new Die(GlassColor.BLUE, 0);
    }

    @Test
    void pickDieTest() {
        for (int i=0; i<9; i++) {
            Assertions.assertTrue((die == (draftPool.pickDie(die)) || draftPool.pickDie(die) == null));
        }
        Assertions.assertNull(draftPool.pickDie(die));
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

    @Test
    void getFreeSpace() {
        this.draftPool.pickDie(4);
        Assertions.assertEquals(8, this.draftPool.getFreeSpace());

    }

    @Test
    void putDieTest() {
        this.draftPool.pickDie(4);
        this.draftPool.pickDie(6);
        this.draftPool.putDie(die);
        Assertions.assertEquals(8, this.draftPool.getFreeSpace());
        Assertions.assertEquals(die, this.draftPool.pickDie(die));
    }
}