package it.polimi.ingsw.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class RoundTrackTest {

    private RoundTrack roundTrack;

    @BeforeEach
    void setUp() {
        this.roundTrack = new RoundTrack(10);
    }

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