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
        draftPool.putDie(die);
        Assertions.assertEquals(die, draftPool.pickDie(die));
        Assertions.assertNull(draftPool.pickDie(new Die(GlassColor.GREEN, 9)));
    }

    @Test
    void pickDie1Test() {
        draftPool.putDie(die);
        Assertions.assertEquals(die, draftPool.pickDie(0));
        Assertions.assertThrows(IndexOutOfBoundsException.class, ()->draftPool.pickDie(0));
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
    void getFreeSpaceTest() {
        // TODO implement Context with MATCH
        Assertions.assertThrows(NullPointerException.class, ()->draftPool.getFreeSpace());
    }

    @Test
    void putDieTest() {
        this.draftPool.putDie(die);
        Assertions.assertEquals(die, this.draftPool.pickDie(die));
    }

}