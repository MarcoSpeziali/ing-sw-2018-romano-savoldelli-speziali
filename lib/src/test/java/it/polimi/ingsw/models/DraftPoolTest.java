package it.polimi.ingsw.models;

import it.polimi.ingsw.RandomParametersExtension;
import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.Match;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(RandomParametersExtension.class)
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
    void getFreeSpaceTest(@RandomParametersExtension.Random int numberOfPlayers) {
        Match match = mock(Match.class);
        when(match.getNumberOfPlayer()).thenReturn(numberOfPlayers);

        Context.getSharedInstance().put(Context.MATCH, match);

        Assertions.assertEquals(numberOfPlayers * 2 + 1, draftPool.getFreeSpace());
    }

    @Test
    void putDieTest() {
        this.draftPool.putDie(die);
        Assertions.assertEquals(die, this.draftPool.pickDie(die));
    }
}