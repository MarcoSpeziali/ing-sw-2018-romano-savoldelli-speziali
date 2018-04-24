package it.polimi.ingsw.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DraftPoolTest {

    private DraftPool draftPool = new DraftPool(4);
    private Die die =  mock(Die.class);

    @Test
    void pickDieTest() {
        Assertions.assertEquals(die, draftPool.pickDie(die));
    }

    @Test
    void pickDie1() {
    }

    @Test
    void getDice() {
    }

    @Test
    void getNumberOfDice() {
    }
}


