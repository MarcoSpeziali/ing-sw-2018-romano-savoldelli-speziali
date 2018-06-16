package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class RoundTrackTest {

    private RoundTrack roundTrack;
    private byte numberOfRounds = 10;
    private Die die1 = new Die(GlassColor.BLUE, 4);
    private Die die2 = new Die(GlassColor.RED, 2);
    private Die die3 = new Die(GlassColor.GREEN, 1);

    @BeforeEach
    void setUp() {
        roundTrack = new RoundTrack(numberOfRounds);
    }

    @Test
    void pickDie1() {
        roundTrack.setDieForRoundAtIndex(die2, 1, 0);
        Die die = roundTrack.pickDie(0x00000100);

        Assertions.assertEquals(die2, die);
    }
}