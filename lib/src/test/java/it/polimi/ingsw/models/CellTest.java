package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.locations.AlreadyPutException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class   CellTest {

    private Cell coloredCell;
    private Cell shadedCell;
    private Cell blankCell;
    private Die die;

    @BeforeEach
    void setUp() {
        this.coloredCell = new Cell(0, GlassColor.BLUE);
        this.shadedCell = new Cell(4, null);
        this.blankCell = new Cell(0, null);
        this.die = mock(Die.class);
    }

    @Test
    void getColorTest(){

        Assertions.assertEquals(GlassColor.BLUE, coloredCell.getColor());
        Assertions.assertNull(shadedCell.getColor());
        Assertions.assertNull(blankCell.getColor());
    }

    @Test
    void getDieTest() {
        blankCell.putDie(die);
        Assertions.assertSame(die, blankCell.getDie());
    }

    @Test
    void getShadeTest() {
        Assertions.assertEquals(Integer.valueOf(4), shadedCell.getShade());
        Assertions.assertEquals(Integer.valueOf(0), coloredCell.getShade());
        Assertions.assertEquals(Integer.valueOf(0), blankCell.getShade());
    }

    @Test
    void putDieTest() {
        blankCell.putDie(this.die);
        Assertions.assertTrue(blankCell.isOccupied());
        Assertions.assertThrows(AlreadyPutException.class, () ->
                blankCell.putDie(this.die));
    }

    @Test
    void pickDieTest() {
        Assertions.assertNull(blankCell.pickDie());
        blankCell.putDie(this.die);
        Assertions.assertEquals(this.die, blankCell.pickDie());
    }

    @Test
    void isOccupiedTest() {
        Assertions.assertFalse(blankCell.isOccupied());
        blankCell.putDie(this.die);
        Assertions.assertTrue(blankCell.isOccupied());
    }

    @Test
    void getNumberOfDiceTest() {
        Assertions.assertEquals(0, blankCell.getNumberOfDice());
        blankCell.putDie(die);
        Assertions.assertEquals(1, blankCell.getNumberOfDice());
    }

    @Test
    void getFreeSpaceTest() {
        Assertions.assertEquals(1, blankCell.getFreeSpace());
        blankCell.putDie(die);
        Assertions.assertEquals(0, blankCell.getFreeSpace());
    }

    @Test
    void isBlankTest() {
        Assertions.assertTrue(blankCell.isBlank());
        Assertions.assertFalse(coloredCell.isBlank());
        Assertions.assertFalse(shadedCell.isBlank());
    }

    @Test
    void canFitDieTest() {
        when(die.getShade()).thenReturn(5);
        when(die.getColor()).thenReturn(GlassColor.BLUE);
        Assertions.assertTrue(coloredCell.matchesOrBlank(die, false, false));
        Assertions.assertFalse(shadedCell.matchesOrBlank(die, false, false));
        Assertions.assertTrue(blankCell.matchesOrBlank(die, false, false));

    }
}