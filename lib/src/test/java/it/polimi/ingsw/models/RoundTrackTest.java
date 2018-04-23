package it.polimi.ingsw.models;

import it.polimi.ingsw.core.Die;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class RoundTrackTest {

    private RoundTrack roundTrack = new RoundTrack();

    @Test
    void getDieAtIndexTest() {
        for (int i = 0; i < 10; i++) {
            Assertions.assertNull(roundTrack.getDieAtIndex(i));
        }
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> roundTrack.getDieAtIndex(-13));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> roundTrack.getDieAtIndex(11));
        Die die = mock(Die.class);
        roundTrack.setDieForCurrentRound(die);
        Assertions.assertEquals(die, roundTrack.getDieAtIndex(0));

    }

    @Test
    void setDieForCurrentRoundTest() {
        for (int i = 0; i < 10; i++) {
            Die die = mock(Die.class);
            roundTrack.setDieForCurrentRound(die);
            Assertions.assertEquals(die, roundTrack.getDieAtIndex(i));
        }
    }

}