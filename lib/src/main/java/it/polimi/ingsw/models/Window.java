package it.polimi.ingsw.models;

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
        List<Integer> locations = getLocations();
        if (locations.isEmpty()) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if ((i == rows - 1 || j == columns - 1) &&
                                    (cellMatches(die, i, j) ||
                                    (ignoreColor && !ignoreShade) ||
                                    (!ignoreColor && ignoreShade) ||
                                    cellIsBlank(i, j)
                                    )
                            ) {
                        admitted.add(i * columns + j);
                    }
                }
            }
        }
        else {
            for (int i: locations) {
                locations.get(i);
            }

        /*(!(cells[i+1][j].pickDie().getShade().equals(die.getShade())) ||
                !(cells[i][j+1].pickDie().getShade().equals(die.getShade())) ||
                !(cells[i-1][j].pickDie().getShade().equals(die.getShade())) ||
                !(cells[i][j-1].pickDie().getShade().equals(die.getShade())) ||
                !(cells[i+1][j].pickDie().getColor().equals(die.getColor())) ||
                !(cells[i][j+1].pickDie().getColor().equals(die.getColor())) ||
                !(cells[i-1][j].pickDie().getColor().equals(die.getColor())) ||
                !(cells[i][j-1].pickDie().getColor().equals(die.getColor()))
                || ignoreAdjacency) ||
                (cells[i][j].getCellColor().equals(die.getColor()) || ignoreColor) ||

                (cells[i][j].getShade().equals(die.getShade()) || ignoreShade) ||

                )+
          */
        return admitted;
    }
    return null;
    }

    @Override
    public void putDie(Die die, Integer location) {
        this.cells[location/rows][location%rows].putDie(die);
    }

    @Override
    public List<Integer> getLocations() {
        List<Integer> locations = new LinkedList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns ; j++) {
                if (cells[i][j].isOccupied())
                    locations.add(i*columns+j);
            }
        }
        return locations;
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
    public Die pickDie(Die die) {
        for (int i = 0; i < rows ; i++) {
            for (int j = 0; j < columns; j++) {
                if (cells[i][j].pickDie().equals(die)) {
                    return cells[i][j].pickDie();
                }
            }
        }
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

    private boolean cellMatches(Die die, int i, int j) {
        if (die.getColor().equals(cells[i][j].getCellColor())) {
            return true;
        } else return die.getShade().equals(cells[i][j].getShade());
    }

    private boolean cellIsBlank(int i, int j) {
        return (cells[i][j].getCellColor() == null && cells[i][j].getShade() == 0);
    }

    private boolean isEdge(int i, int j) {
        return false;
    }
}


