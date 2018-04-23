package it.polimi.ingsw.models;

import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.locations.ChoosablePickLocation;
import it.polimi.ingsw.core.locations.RestrictedChoosablePutLocation;

import java.util.List;

// FIXME: Window dovrebbe implementare RestrictedChoosablePutLocation, ChoosablePickLocation
public class Window implements RestrictedChoosablePutLocation, ChoosablePickLocation {

    private int difficulty;
    private int rows;
    private int columns;
    private String id;
    private Window sibling;
    // FIXME: Un'implementazione a matrice non è più comoda?
    private Cell[] cells;

    public Window(int difficulty, int rows, int columns, String id, Window sibling, Cell[] cells) {
        this.difficulty = difficulty;
        this.rows = rows;
        this.columns = columns;
        this.id = id;
        this.sibling = sibling;
        this.cells = cells;
    }

    public Window getSibling() {
        return sibling;
    }

    public Cell[] getCells() {
        return cells;
    }

    @Override
    public List<Integer> getPossiblePositionsForDie(Die die, Boolean ignoreColor, Boolean ignoreShade, Boolean ignoreAdjacency) {
        return null;
    }

    @Override
    public void putDie(Die die, Integer location) {

    }

    @Override
    public List<Integer> getLocations() {
        return null;
    }

    @Override
    public List<Die> getDice() {
        return null;
    }

    @Override
    public int getFreeSpace() {
        return 0;
    }

    @Override
    public Die pickDie(Die die) {
        return null;
    }

    @Override
    public Die pickDie(Integer location) {
        return null;
    }

    @Override
    public int getNumberOfDice() {
        return 0;
    }
}
