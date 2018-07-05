package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class RoundTrackTest {
    private RoundTrack roundTrack;
    private Die die1 = new Die(4, GlassColor.BLUE);
    private Die die2 = new Die(2, GlassColor.RED);
    private Die die3 = new Die(1, GlassColor.GREEN);

    @BeforeEach
    void setUp() {
        byte numberOfRounds = 10;
        roundTrack = new RoundTrack(numberOfRounds);
    }

    @Test
    void getDiceForRound() {
        roundTrack.setDieForRoundAtIndex(die1, (byte) 1, (byte) 0);

        Assertions.assertEquals(roundTrack.getDiceForRound(1).get(0), die1);
    }

    @Test
    void getDiceForRoundAtIndex() {
        roundTrack.setDieForRoundAtIndex(die1, (byte) 2, (byte) 0);
        roundTrack.setDieForRoundAtIndex(die3, (byte) 2, (byte) 1);
        Assertions.assertEquals(roundTrack.getDiceForRoundAtIndex(2, 1), die3);
    }

    @Test
    void setDieForRoundAtIndex() {
        roundTrack.setDieForRoundAtIndex(die1, (byte) 1, (byte) 0);
        roundTrack.setDieForRoundAtIndex(die3, (byte) 1, (byte) 1);
        Assertions.assertEquals(die3, roundTrack.getDiceForRoundAtIndex(1, 1));
    }

    @Test
    void addDieForRound() {
        roundTrack.setDieForRoundAtIndex(die2, (byte) 1, (byte) 0);
        roundTrack.addDieForRound(die3, (byte) 1);
        Assertions.assertEquals(die3, roundTrack.getDiceForRoundAtIndex(1, 1));
    }

    @Test
    void pickDie() {
        roundTrack.setDieForRoundAtIndex(die3, (byte) 2, (byte) 0);

        Assertions.assertSame(die3, roundTrack.pickDie(die3));
    }

    @Test
    void pickDie1() {
        roundTrack.setDieForRoundAtIndex(die2, (byte) 1, (byte) 0);
        Die die = roundTrack.pickDie(0x00000100);
        Assertions.assertEquals(die2, die);
    }

    @Test
    void putDie() {
        roundTrack.putDie(die1, 0x00000200);
        Assertions.assertEquals(die1, roundTrack.getDiceForRoundAtIndex(2, 0));
    }

    @Test
    void getLocations() {
        roundTrack.setDieForRoundAtIndex(die1, (byte) 1, (byte) 0);
        roundTrack.setDieForRoundAtIndex(die3, (byte) 2, (byte) 0);
        roundTrack.setDieForRoundAtIndex(die2, (byte) 2, (byte) 1);
        Assertions.assertEquals(0x00000200, roundTrack.getFullLocations().get(1).intValue());
        Assertions.assertEquals(0x00000201, roundTrack.getFullLocations().get(2).intValue());
        Assertions.assertEquals(die3, roundTrack.pickDie(roundTrack.getFullLocations().get(1)));
    }

    @Test
    void getDice() {
        roundTrack.setDieForRoundAtIndex(die1, (byte) 1, (byte) 0);
        roundTrack.setDieForRoundAtIndex(die3, (byte) 2, (byte) 0);
        roundTrack.setDieForRoundAtIndex(die2, (byte) 2, (byte) 1);
        roundTrack.setDieForRoundAtIndex(die1, (byte) 9, (byte) 0);
        roundTrack.setDieForRoundAtIndex(die3, (byte) 6, (byte) 0);
        List<Die> list = roundTrack.getDice();
        Assertions.assertEquals(die3, list.get(3));
        Assertions.assertEquals(5, list.size());

    }

    @Test
    void getNumberOfDice() {
        roundTrack.setDieForRoundAtIndex(die1, (byte) 1, (byte) 0);
        roundTrack.setDieForRoundAtIndex(die3, (byte) 2, (byte) 0);
        roundTrack.setDieForRoundAtIndex(die2, (byte) 2, (byte) 1);
        Assertions.assertEquals(3, roundTrack.getNumberOfDice());
    }

    @Test
    void getFreeSpace() {
        roundTrack.setDieForRoundAtIndex(die1, (byte) 1, (byte) 0);
        roundTrack.setDieForRoundAtIndex(die3, (byte) 2, (byte) 0);
        roundTrack.setDieForRoundAtIndex(die2, (byte) 2, (byte) 1);
        roundTrack.setDieForRoundAtIndex(die1, (byte) 5, (byte) 0);
        Assertions.assertEquals(1266, roundTrack.getFreeSpace());
    }
}