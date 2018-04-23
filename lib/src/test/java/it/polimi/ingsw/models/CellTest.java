package it.polimi.ingsw.models;

import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.locations.AlreadyPutException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class CellTest {

    private Cell coloredCell = new Cell(GlassColor.BLUE);
    private Cell shadedCell = new Cell(4);
    private Cell blankCell = new Cell();
    private Die die = mock(Die.class);

    @Test
    void getCellColorTest(){

        Assertions.assertEquals(GlassColor.BLUE, coloredCell.getCellColor());
        Assertions.assertNull(shadedCell.getCellColor());
        Assertions.assertNull(blankCell.getCellColor());
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
    void getNumberOfDice() {
        Assertions.assertEquals(0, blankCell.getNumberOfDice());
        blankCell.putDie(die);
        Assertions.assertEquals(1, blankCell.getNumberOfDice());
    }
}