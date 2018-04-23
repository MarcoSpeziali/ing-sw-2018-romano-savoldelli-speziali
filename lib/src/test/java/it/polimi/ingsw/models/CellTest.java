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
        Assertions.assertThrows(NullPointerException.class, () ->
                shadedCell.getCellColor()
        );
        Assertions.assertThrows(NullPointerException.class, () ->
                blankCell.getCellColor()
        );
    }

    @Test
    void getShadeTest() {
        Assertions.assertEquals(4, shadedCell.getShade());
        Assertions.assertThrows(NullPointerException.class, () ->
                coloredCell.getShade()
        );
        Assertions.assertThrows(NullPointerException.class, () ->
                blankCell.getShade()
        );
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
        Assertions.assertThrows(NullPointerException.class, () -> blankCell.pickDie());
        blankCell.putDie(this.die);
        Assertions.assertEquals(this.die, blankCell.pickDie());
    }

    @Test
    void isOccupiedTest() throws AlreadyPutException {
        Assertions.assertFalse(blankCell.isOccupied());
        blankCell.putDie(this.die);
        Assertions.assertTrue(blankCell.isOccupied());
    }
}