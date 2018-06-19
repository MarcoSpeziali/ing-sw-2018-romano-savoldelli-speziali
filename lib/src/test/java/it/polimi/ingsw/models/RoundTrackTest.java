package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.listeners.OnDiePickedListener;
import it.polimi.ingsw.listeners.OnDiePutListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

class RoundTrackTest {
    private RoundTrack roundTrack;
    private byte numberOfRounds = 10;
    private Die die1 = new Die(GlassColor.BLUE, 4);
    private Die die2 = new Die(GlassColor.RED, 2);
    private Die die3 = new Die(GlassColor.GREEN, 1);
    private List<OnDiePickedListener> onDiePickedListeners;
    private List<OnDiePutListener> onDiePutListeners;


    @BeforeEach
    void setUp() {

        roundTrack = new RoundTrack(numberOfRounds);
        onDiePutListeners = new LinkedList<>();
        onDiePickedListeners = new LinkedList<>();

    }

    @Test
    void getDiceForRound() {
        roundTrack.setDieForRoundAtIndex(die1, 1, 0);

        Assertions.assertEquals(roundTrack.getDiceForRound(1).get(0), die1);
        System.out.println(roundTrack.getNumberOfDice());
    }

    @Test
    void getDiceForRoundAtIndex() {

        roundTrack.setDieForRoundAtIndex(die1, 2, 0);
        roundTrack.setDieForRoundAtIndex(die3, 2, 1);
        Assertions.assertEquals(roundTrack.getDiceForRoundAtIndex(2, 1), die3);

    }

    @Test
    void setDieForRoundAtIndex() {
        roundTrack.setDieForRoundAtIndex(die1, 1, 0);
        roundTrack.setDieForRoundAtIndex(die3, 1, 1);
        Assertions.assertEquals(die3, roundTrack.getDiceForRoundAtIndex(1, 1));

    }

    @Test
    void addDieForRound() {
        roundTrack.setDieForRoundAtIndex(die2, 1, 0);
        roundTrack.addDieForRound(die3, 1);
        Assertions.assertEquals(die3, roundTrack.getDiceForRoundAtIndex(1, 1));
    }

    @Test
    void pickDie() {
        roundTrack.setDieForRoundAtIndex(die3, 2, 0);
        try {
            roundTrack.pickDie(die3);
            assert false;
        }
        catch (UnsupportedOperationException exception) {
            assert true;
        }
    }

    @Test
    void pickDie1() {
        roundTrack.setDieForRoundAtIndex(die2, 1, 0);
        System.out.println(roundTrack.getNumberOfDice());
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
        roundTrack.setDieForRoundAtIndex(die1, 1, 0);
        roundTrack.setDieForRoundAtIndex(die3, 2, 0);
        roundTrack.setDieForRoundAtIndex(die2, 2, 1);
        Assertions.assertEquals(0x00000200, roundTrack.getLocations().get(1).intValue());
        Assertions.assertEquals(0x00000201, roundTrack.getLocations().get(2).intValue());
        Assertions.assertEquals(die3, roundTrack.pickDie(roundTrack.getLocations().get(1)));
    }

    @Test
    void getDice() {
        roundTrack.setDieForRoundAtIndex(die1, 1, 0);
        roundTrack.setDieForRoundAtIndex(die3, 2, 0);
        roundTrack.setDieForRoundAtIndex(die2, 2, 1);
        roundTrack.setDieForRoundAtIndex(die1, 9, 0);
        roundTrack.setDieForRoundAtIndex(die3, 6, 0);
        List<Die> list = roundTrack.getDice();
        Assertions.assertEquals(die3, list.get(3));
        Assertions.assertEquals(5, list.size());

    }

    @Test
    void getNumberOfDice() {
        roundTrack.setDieForRoundAtIndex(die1, 1, 0);
        roundTrack.setDieForRoundAtIndex(die3, 2, 0);
        roundTrack.setDieForRoundAtIndex(die2, 2, 1);
        Assertions.assertEquals(3, roundTrack.getNumberOfDice());
    }

    @Test
    void getFreeSpace() {
        roundTrack.setDieForRoundAtIndex(die1, 1, 0);
        roundTrack.setDieForRoundAtIndex(die3, 2, 0);
        roundTrack.setDieForRoundAtIndex(die2, 2, 1);
        roundTrack.setDieForRoundAtIndex(die1, 5, 0);
        Assertions.assertEquals(1266, roundTrack.getFreeSpace());
    }

    @Test
    void addPickListener() {
    }

    @Test
    void addPutListener1() {
    }

    @Test
    void removePickListener() {
    }

    @Test
    void removePutListener1() {
    }
}