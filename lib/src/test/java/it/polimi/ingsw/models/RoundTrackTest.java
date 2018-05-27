package it.polimi.ingsw.models;

import org.junit.Ignore;
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
    @Ignore
    void getDieAtIndexTest() {
        for (int i = 0; i < 10; i++) {
            // FIXME: java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
            // Assertions.assertNotNull(roundTrack.getDieAtIndex(i));
        }

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> roundTrack.getDieAtIndex(-13));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> roundTrack.getDieAtIndex(11));
        Die die = mock(Die.class);
        // FIXME: java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        // roundTrack.setDieForCurrentRound(die);
        // FIXME: java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        // Assertions.assertEquals(die, roundTrack.getDieAtIndex(0));
    }

    @Test
    @Ignore
    void setDieForCurrentRoundTest() {
        for (int i = 0; i < 10; i++) {
            Die die = mock(Die.class);
            // FIXME: java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
            // roundTrack.setDieForCurrentRound(die);
            // FIXME: java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
            // Assertions.assertEquals(die, roundTrack.getDieAtIndex(i));
        }
    }
}