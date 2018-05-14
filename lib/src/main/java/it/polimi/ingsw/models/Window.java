package it.polimi.ingsw.models;

import it.polimi.ingsw.core.locations.ChoosablePickLocation;
import it.polimi.ingsw.core.locations.RestrictedChoosablePutLocation;
import it.polimi.ingsw.utils.IterableRange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Window implements RestrictedChoosablePutLocation, ChoosablePickLocation {

    private int difficulty;
    private int rows;
    private int columns;
    private String id;
    private Window sibling;
    private Cell[][] cells;

    public Window(int difficulty, int rows, int columns, String id, Window sibling, Cell[][] cells) {
        this.difficulty = difficulty;
        this.rows = rows;
        this.columns = columns;
        this.id = id;
        this.sibling = sibling;
        this.cells = cells;
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

    /*@Override
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
                if (((adjacencyRespected(die, i / rows, i % rows) || ignoreAdjacency)) &&
                        ((cellMatches(die, i / rows, i % rows) || (ignoreColor || ignoreShade))) ||
                        cellIsBlank(i / rows, i % rows)) {
                    admitted[i / rows][i % rows] = true;
                }
            }
            return admitted;
        }
    }*/

    @Override
    public List<Integer> getPossiblePositionsForDie(Die die, Boolean ignoreColor, Boolean ignoreShade, Boolean ignoreAdjacency) {
        if (die.getShade() == 0) {
            throw new IllegalArgumentException("Die's shade must be set!");
        }

        if (die.getColor() == null) {
            throw new IllegalArgumentException("Die's color must be set!");
        }

        if (this.getNumberOfDice() == 0) {
            return this.getEdgesPositions(die, ignoreColor, ignoreShade, ignoreAdjacency);
        }

        List<Integer> availablePositions = new ArrayList<>(this.rows * this.columns);

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                if (!availablePositions.contains(i * this.columns + j) &&
                        this.cells[i][j].canFitDie(die, ignoreColor, ignoreShade) &&
                        (ignoreAdjacency || checkAdjacency(die, i, j, ignoreColor, ignoreShade))) {
                    availablePositions.add(i * this.columns + j);
                }
            }
        }

        return availablePositions;
    }

    @Override
    public void putDie(Die die, Integer location) {
        this.cells[location / columns][location % columns].putDie(die);
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
        return Arrays.stream(this.cells)
                .flatMap(Arrays::stream)
                .map(Cell::getDie)
                .collect(Collectors.toList());
    }

    @Override
    public int getFreeSpace() {
        return Math.toIntExact(Arrays.stream(this.cells)
                .flatMap(Arrays::stream)
                .filter(cell -> !cell.isOccupied())
                .count());
    }

    @Override
    public Die pickDie(Die die) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (die.equals(cells[i][j].getDie())) {
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
        return rows*columns - getFreeSpace();
    }

    /*private boolean adjacencyRespected(Die die, int i, int j) {
        if (i == rows - 1) {
            if (j == columns - 1)
                return !cellMatches(die, i, j - 1) &&
                        !cellMatches(die, i - 1, j);
            if (j == 0)
                return !cellMatches(die, i, j + 1) &&
                        !cellMatches(die, i - 1, j);
            else
                return !cellMatches(die, i, j + 1) &&
                        !cellMatches(die, i, j - 1) &&
                        !cellMatches(die, i - 1, j);
        }
        if (i == 0) {
            if (j == columns - 1)
                return !cellMatches(die, i, j - 1) &&
                        !cellMatches(die, i + 1, j);
            if (j == 0)
                return !cellMatches(die, i, j + 1) &&
                        !cellMatches(die, i + 1, j);
            else
                return !cellMatches(die, i, j + 1) &&
                        !cellMatches(die, i, j - 1) &&
                        !cellMatches(die, i + 1, j);

        }
        return !cellMatches(die, i, j + 1) &&
                !cellMatches(die, i, j - 1) &&
                !cellMatches(die, i - 1, j) &&
                !cellMatches(die, i + 1, j);
    }*/

    private boolean checkAdjacency(Die die, int i, int j, boolean ignoreColor, boolean ignoreShade) {
        return  canHaveNeighbour(die, i, j + 1, ignoreColor, ignoreShade) &&
                canHaveNeighbour(die, i, j - 1, ignoreColor, ignoreShade) &&
                canHaveNeighbour(die, i - 1, j, ignoreColor, ignoreShade) &&
                canHaveNeighbour(die, i + 1, j, ignoreColor, ignoreShade);
    }

    private boolean canHaveNeighbour(Die neighbourCandidate, int i, int j, boolean ignoreColor, boolean ignoreShade) {
        if (i < 0 || j < 0 || i > rows - 1 || j > columns - 1) {
            return true;
        }

        return this.cells[i][j].canFitDie(neighbourCandidate, ignoreShade, ignoreColor);
    }

    /*
    private boolean[][] getInitialPositions(Die die, boolean ignoreColor, boolean ignoreShade) {
        boolean[][] admitted = new boolean[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i == rows - 1 || j == columns - 1) &&
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
    */
    private List<Integer> getEdgesPositions(Die die, boolean ignoreColor, boolean ignoreShade, Boolean ignoreAdjacency) {
        List<Integer> edges = new ArrayList<>((this.rows + this.columns) * 2);

        if (ignoreAdjacency) {
            return new IterableRange<>(
                    0,
                    (this.rows + this.columns) * 2,
                    IterableRange.INTEGER_INCREMENT_FUNCTION
            ).stream().collect(Collectors.toList());
        }

        for (int i = 0; i < this.rows; i++) {
            // first column
            if (this.getCells()[i][0].canFitDie(die, ignoreColor, ignoreShade)) {
                edges.add(i * this.columns);
            }

            // last column
            if (this.getCells()[i][this.columns - 1].canFitDie(die, ignoreColor, ignoreShade)) {
                edges.add(i * this.columns + this.columns - 1);
            }
        }

        for (int j = 1; j < this.columns - 1; j++) {
            // first row
            if (this.getCells()[0][j].canFitDie(die, ignoreColor, ignoreShade)) {
                edges.add(j);
            }

            // last row
            if (this.getCells()[this.rows - 1][j].canFitDie(die, ignoreColor, ignoreShade)) {
                edges.add((this.rows - 1) * this.columns + j);
            }
        }
        return edges;
    }
}


