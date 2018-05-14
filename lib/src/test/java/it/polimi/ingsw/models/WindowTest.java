package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.locations.AlreadyPutException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;

class WindowTest {

    private Window window;
    private Cell[][] cells;
    private Die die;

    @BeforeEach
    void setUp() {
        this.die = new Die(GlassColor.YELLOW,5);

        this.cells = new Cell[][] { // 0, 1, 4, 5(edge), 11
                {
                    new Cell(0, null),      new Cell(5, null),      new Cell(4, null),       new Cell(0, GlassColor.GREEN)
                },
                {
                    new Cell(0, null),      new Cell(0, null),      new Cell(2, null),       new Cell(0, GlassColor.PURPLE)
                },
                {
                    new Cell(0, GlassColor.BLUE), new Cell(2, null),      new Cell(0, GlassColor.RED),   new Cell(0, GlassColor.YELLOW)
                }
        };

        this.window = new Window(22, 3, 4, "test", null, cells);
    }

    @Test
    void getDifficultyTest() {
        Assertions.assertEquals(22, window.getDifficulty());
    }

    @Test
    void getIdTest() {
        Assertions.assertEquals("test", window.getId());
    }

    @Test
    void getSiblingTest() {
        Assertions.assertNull(window.getSibling());
    }

    @Test
    void getCellsTest() {
        Assertions.assertEquals(cells, window.getCells());
    }

    @Test
    void getPossiblePositionsForDie() {
        List<Integer> a = List.of(0,1,4,11);
        List<Integer> b = List.of(0,5);
        Assertions.assertTrue(window.getPossiblePositionsForDie(die,
                false, false, false).containsAll(a));
        Assertions.assertEquals(4, window.getPossiblePositionsForDie(die,
                false, false, false).size());
        window.putDie(die, 1);
        Assertions.assertFalse(window.getPossiblePositionsForDie(new Die(GlassColor.RED, 5),
                false, false, false).contains(1));
        Assertions.assertTrue(window.getPossiblePositionsForDie(new Die(GlassColor.GREEN, 5),
                false, false, false).containsAll(b));
    }

    @Test
    void putDieTest() {
        window.putDie(die, 7);
        Assertions.assertTrue(window.getDice().contains(die));
        Assertions.assertThrows(AlreadyPutException.class, ()-> window.putDie(die, 7));
        Assertions.assertThrows(IndexOutOfBoundsException.class, ()-> window.putDie(die, 20));
        Assertions.assertThrows(IndexOutOfBoundsException.class, ()-> window.putDie(die, -1));
    }

    @Test
    void getLocations() {
        Assertions.assertTrue(window.getLocations().isEmpty());
        window.putDie(die, 1);
        Assertions.assertFalse(window.getLocations().isEmpty());
        Assertions.assertTrue(window.getLocations().contains(1));
        Assertions.assertFalse(window.getLocations().contains(0));
    }

    @Test
    void getDiceTest() {
        window.putDie(die, 7);
        window.putDie(new Die(GlassColor.RED, 4), 0);
        Assertions.assertTrue(window.getDice().contains(this.die));
        window.pickDie(die);
        Assertions.assertFalse(window.getDice().contains(die));
        window.pickDie(new Die(GlassColor.RED, 4));
        Assertions.assertFalse(window.getDice().contains(new Die(GlassColor.RED,4)));
        Assertions.assertEquals(12,window.getDice().size()); //Ha senso che venga restituita una lista di null?
    }

    @Test
    void getFreeSpaceTest() {
        Assertions.assertEquals(12, window.getFreeSpace());
        window.putDie(die, 3);
        Assertions.assertEquals(11, window.getFreeSpace());
    }

    @Test
    void pickDieTest() {
        window.putDie(die, 0);
        Assertions.assertEquals(die, window.pickDie(0));
        Assertions.assertNull(window.getCells()[0][0].getDie());
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> window.pickDie(30));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> window.pickDie(-2));

    }

    @Test
    void pickDie1Test() {
        window.putDie(die, 1);
        Assertions.assertTrue(window.getDice().contains(die));
        int num = window.getNumberOfDice();
        window.pickDie(die);
        Assertions.assertEquals(window.getNumberOfDice(), num - 1);
    }

    @Test
    void getNumberOfDiceTest() {
        Assertions.assertEquals(0, window.getNumberOfDice());
        window.putDie(mock(Die.class), 4);
        Assertions.assertEquals(1,window.getNumberOfDice());
    }
}