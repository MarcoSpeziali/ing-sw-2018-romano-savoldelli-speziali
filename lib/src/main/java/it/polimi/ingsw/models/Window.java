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

    public int getDifficulty() {
        return difficulty;
    }

    public String getId() {
        return id;
    }

    public Window getSibling() {
        return sibling;
    }

    public Cell[][] getCells() {
        return cells;
    }

    @Override
    public boolean[][] getPossiblePositionsForDie(Die die, Boolean ignoreColor, Boolean ignoreShade, Boolean ignoreAdjacency) {

        List<Integer> locations = this.getLocations();
        boolean[][] admitted = new boolean[rows][columns];

        if (die.getShade() == 0) {
            throw new IllegalArgumentException("Die's shade must be set!");
        }

        if (locations.isEmpty()) {
            return getInitialPositions(die, ignoreColor, ignoreShade);
        } else {
            for (int i : locations) {
                if (    ((adjacencyRespected(die, i/rows, i%rows) || ignoreAdjacency)) &&
                        ((cellMatches(die, i/rows, i%rows) || (ignoreColor || ignoreShade))) ||
                        cellIsBlank(i/rows, i%rows)) {
                    admitted[i/rows][i%rows] = true;
                }
            }
            return admitted;
        }
    }

    @Override
    public void putDie(Die die, Integer location) {
        this.cells[location / rows][location % rows].putDie(die);
    }

    @Override
    public List<Integer> getLocations() {
        List<Integer> locations = new LinkedList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (cells[i][j].isOccupied())
                    locations.add(i * columns + j);
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
                    free.add(i * columns + j);
                }
            }
        }
        return free.size();
    }

    @Override
    public Die pickDie(Die die) {
        for (int i = 0; i < rows; i++) {
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
        return cells[location / rows][location % rows].pickDie();
    }

    @Override
    public int getNumberOfDice() {
        return cells.length - getFreeSpace();
    }

    private boolean cellMatches(Die die, int i, int j) {
        if (die.getColor().equals(cells[i][j].getColor())) {
            return true;
        } else return die.getShade().equals(cells[i][j].getShade());
    }

    private boolean adjacencyRespected(Die die, int i, int j) {
        if (i == rows - 1) {
            if (j == columns - 1)
                return  !cellMatches(die, i, j-1) &&
                        !cellMatches(die, i-1, j);
            if (j == 0)
                return  !cellMatches(die, i, j+1) &&
                        !cellMatches(die, i-1, j);
            else
                return  !cellMatches(die, i, j+1) &&
                        !cellMatches(die, i, j-1) &&
                        !cellMatches(die, i-1, j);
        }
        if (i == 0) {
            if (j == columns - 1)
                return  !cellMatches(die, i, j-1) &&
                        !cellMatches(die, i+1, j);
            if (j == 0)
                return  !cellMatches(die, i, j+1) &&
                        !cellMatches(die, i+1, j);
            else
                return  !cellMatches(die, i, j+1) &&
                        !cellMatches(die, i, j-1) &&
                        !cellMatches(die, i+1, j);

        }
        return  !cellMatches(die, i, j+1) &&
                !cellMatches(die, i, j-1) &&
                !cellMatches(die, i-1, j) &&
                !cellMatches(die, i+1, j);
        }

    private boolean cellIsBlank(int i, int j) {
        return (cells[i][j].getColor() == null && cells[i][j].getShade() == 0);
    }

    private boolean[][] getInitialPositions(Die die, boolean ignoreColor, boolean ignoreShade) {
        boolean[][] admitted = new boolean[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i == rows-1 || j == columns-1) &&
                                (cellMatches(die, i, j) ||
                                (ignoreColor && !ignoreShade) ||
                                (!ignoreColor && ignoreShade) ||
                                cellIsBlank(i, j)
                        )
                        ) {
                    admitted[i][j] = true;
                }
            }
        }
        return admitted;
    }
}


