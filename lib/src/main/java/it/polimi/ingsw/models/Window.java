package it.polimi.ingsw.models;

import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.locations.ChoosablePickLocation;
import it.polimi.ingsw.core.locations.RestrictedChoosablePutLocation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Window implements RestrictedChoosablePutLocation, ChoosablePickLocation {

    private int difficulty;
    private int rows;
    private int columns;
    private String id;
    private Window sibling;
    private Cell[][] cells;

    public Window(int difficulty, int rows, int columns, String id, Window sibling) {
        this.difficulty = difficulty;
        this.rows = rows;
        this.columns = columns;
        this.id = id;
        this.sibling = sibling;
        this.cells = new Cell[rows][columns];
    }

    public Window getSibling() {
        return sibling;
    }

    public Cell[][] getCells() {
        return cells;
    }

    @Override
    public List<Integer> getPossiblePositionsForDie(Die die, Boolean ignoreColor, Boolean ignoreShade, Boolean ignoreAdjacency) {
        List<Integer> admitted = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (    (cells[i][j].getCellColor().equals(die.getColor()) || ignoreColor) ||

                        (cells[i][j].getShade().equals(die.getShade()) || ignoreShade) ||

                        (cells[i][j].getCellColor() == null && cells[i][j].getShade() == null) ||

                        (!(cells[i+1][j].getShade().equals(die.getShade())) ||
                        !(cells[i][j+1].getShade().equals(die.getShade())) ||
                        !(cells[i-1][j].getShade().equals(die.getShade())) ||
                        !(cells[i][j-1].getShade().equals(die.getShade())) ||
                        !(cells[i+1][j].getCellColor().equals(die.getColor())) ||
                        !(cells[i][j+1].getCellColor().equals(die.getColor())) ||
                        !(cells[i-1][j].getCellColor().equals(die.getColor())) ||
                        !(cells[i][j-1].getCellColor().equals(die.getColor()))
                        || ignoreAdjacency)
                        ) {
                    admitted.add(i*columns+j);
                }
            }
        }
        return admitted;
    }

    @Override
    public void putDie(Die die, Integer location) {
        this.cells[location/rows][location%rows].putDie(die);
    }

    @Override
    public List<Integer> getLocations() {
        return null;
    }

    @Override
    public List<Die> getDice() {
        List<Die> list = new LinkedList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                list.add(cells[i][j].pickDie());
            }
        }
        return list;
    }

    @Override
    public int getFreeSpace() {
        List<Integer> free = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (!cells[i][j].isOccupied()) {
                    free.add(i*columns+j);
                }
            }
        }
        return free.size();
    }

    @Override
    public Die pickDie(Die die) { //TODO implementazione esatta?
        /*for (int i = 0; i < rows ; i++) {
            for (int j = 0; j < columns; j++) {
                if (cells[i][j].pickDie().equals(die)) {

                }
            }

        }
        */
        return null;
    }

    @Override
    public Die pickDie(Integer location) {
        return cells[location/rows][location%rows].pickDie();
    }

    @Override
    public int getNumberOfDice() {
        return cells.length-getFreeSpace();
    }
}
