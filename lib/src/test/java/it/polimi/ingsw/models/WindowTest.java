package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.locations.FullLocationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;

class WindowTest {

    private Window window;
    private Cell[][] cells;
    private Die die;
    private Window mockWindow;

    @BeforeEach
    void setUp() {
        this.die = new Die(5, GlassColor.YELLOW);
        this.mockWindow = mock(Window.class);

        this.cells = new Cell[][]{ // 0, 1, 4, 5(edge), 11 // 0, 4, 9, 10  no 5,6
                {
                        new Cell(0, null), new Cell(5, null), new Cell(4, null), new Cell(0, GlassColor.GREEN)
                },
                {
                        new Cell(0, null), new Cell(0, null), new Cell(2, null), new Cell(0, GlassColor.PURPLE)
                },
                {
                        new Cell(0, GlassColor.BLUE), new Cell(2, null), new Cell(0, GlassColor.RED), new Cell(0, GlassColor.YELLOW)
                }
        };

        this.window = new Window(22, 3, 4, "test", cells);
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
    void getCellsTest() {
        Assertions.assertEquals(cells, window.getCells());
    }

    @Test
    void getPossiblePositionsForDie() {
        Die d = new Die(2, GlassColor.RED);
        Die d1 = new Die(3, GlassColor.GREEN);
        Die d2 = new Die(2, GlassColor.PURPLE);

        List<Integer> expectedAtStart = List.of(0, 4, 9, 10);
        List<Integer> expectedAfterPutDie = List.of(0, 5, 8);
        List<Integer> expectedIfAdjacencyIsIgnored = List.of(0, 1, 5, 11);


        // Checking Exceptions

        Assertions.assertThrows(IllegalArgumentException.class, () -> window.getPossiblePositionsForDie(
                new Die(4, null), false, false, false)
        );
        Assertions.assertThrows(IllegalArgumentException.class, () -> window.getPossiblePositionsForDie(
                new Die(0, GlassColor.YELLOW), false, false, false)
        );

        // Checking initial available positions:

        Assertions.assertTrue(window.getPossiblePositionsForDie(d,
                false, false, false).containsAll(expectedAtStart));
        Assertions.assertEquals(4, window.getPossiblePositionsForDie(d,
                false, false, false).size());
        Assertions.assertFalse(window.getPossiblePositionsForDie(d,
                false, false, false).contains(5) &&
                window.getPossiblePositionsForDie(d,
                        false, false, false).contains(6));

        // Checking available positions after a die has been put:

        window.putDie(d, 4);

        Assertions.assertTrue(window.getPossiblePositionsForDie(new Die(4, GlassColor.BLUE),
                false, false, false).containsAll(expectedAfterPutDie));
        Assertions.assertEquals(3, window.getPossiblePositionsForDie(new Die(4, GlassColor.BLUE),
                false, false, false).size());
        Assertions.assertTrue(window.getPossiblePositionsForDie(new Die(2, GlassColor.RED),
                false, false, false).contains(9));
        Assertions.assertEquals(1, window.getPossiblePositionsForDie(new Die(2, GlassColor.RED),
                false, false, false).size());

        // Checking available positions with ignoreAdjacency set to true:

        window.putDie(d1, 3);
        window.putDie(d2, 9);

        Assertions.assertTrue(window.getPossiblePositionsForDie(new Die(5, GlassColor.YELLOW),
                false, false, true).containsAll(expectedIfAdjacencyIsIgnored));
        Assertions.assertEquals(4, window.getPossiblePositionsForDie(new Die(5, GlassColor.YELLOW),
                false, false, true).size());

    }

    @Test
    void putDieTest() {
        window.putDie(die, 7);
        Assertions.assertTrue(window.getDice().contains(die));
        Assertions.assertThrows(FullLocationException.class, () -> window.putDie(die, 7));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> window.putDie(die, 20));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> window.putDie(die, -1));
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
        window.putDie(new Die(4, GlassColor.RED), 0);
        Assertions.assertTrue(window.getDice().contains(this.die));
        window.pickDie(die);
        Assertions.assertFalse(window.getDice().contains(die));
        Assertions.assertEquals(12, window.getDice().size()); //Ha senso che venga restituita una lista di null?
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
        Assertions.assertSame(die, window.pickDie(0));
        window.putDie(die, 0);
        Assertions.assertNull(window.pickDie(new Die(5, GlassColor.YELLOW)));
        Assertions.assertNotNull(window.getCells()[0][0].getDie());
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
        Assertions.assertNull(window.pickDie(die));
    }

    @Test
    void getNumberOfDiceTest() {
        Assertions.assertEquals(0, window.getNumberOfDice());
        window.putDie(mock(Die.class), 4);
        Assertions.assertEquals(1, window.getNumberOfDice());
    }
}