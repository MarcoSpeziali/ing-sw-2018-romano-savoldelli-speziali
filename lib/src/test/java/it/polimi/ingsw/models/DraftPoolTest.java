package it.polimi.ingsw.models;

import it.polimi.ingsw.RandomParametersExtension;
import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.Match;
import it.polimi.ingsw.listeners.OnDiePickedListener;
import it.polimi.ingsw.listeners.OnDiePutListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(RandomParametersExtension.class)
class DraftPoolTest {
    private DraftPool draftPool;
    private Die die;
    private Bag bag;
    private List<OnDiePickedListener> onDiePickedListeners;
    private OnDiePickedListener onDiePickedListener;
    @BeforeEach
    void setUp() {
        bag = new Bag(18);
        draftPool = new DraftPool();
        die = new Die(GlassColor.BLUE, 0);
        onDiePickedListeners = new LinkedList<>();
        onDiePickedListener = mock(OnDiePickedListener.class);

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

    @Test
    void addPutListenerTest() {
        List<OnDiePutListener> onDiePutListeners = new LinkedList<>();
        Assertions.assertTrue(onDiePutListeners.isEmpty());
        Die die1 = new Die(GlassColor.BLUE, 3);

        OnDiePutListener onDiePutListener = new OnDiePutListener() {
            @Override
            public void onDiePut(Die die, Integer location) {
                die = die1;
                location = 3;
            }
        };
        onDiePutListeners.add(onDiePutListener);
        OnDiePutListener onDiePutListener1 = onDiePutListeners.get(0);
        Assertions.assertTrue(onDiePutListeners.size()!=0);
        Assertions.assertEquals(onDiePutListener, onDiePutListener1);

    }

    @Test
    void addPickListenerTest() {
        //Assertions.assertTrue(onDiePickedListeners.isEmpty());
        draftPool.addPickListener(onDiePickedListener);
        System.out.println(onDiePickedListener);
        Assertions.assertFalse(onDiePickedListeners.isEmpty());
        //Assertions.assertEquals(onDiePickedListener1, onDiePickedListener);

    }
}