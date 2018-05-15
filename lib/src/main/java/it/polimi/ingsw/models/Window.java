package it.polimi.ingsw.models;

import it.polimi.ingsw.core.locations.ChoosablePickLocation;
import it.polimi.ingsw.core.locations.RestrictedChoosablePutLocation;
import it.polimi.ingsw.utils.IterableRange;

import java.util.*;
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

    public void setSibling(Window sibling) {
        this.sibling = sibling;
    }

    public Cell[][] getCells() {
        return cells;
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
            if (this.getCells()[i][0].matchesOrBlank(die, ignoreColor, ignoreShade)) {
                edges.add(i * this.columns);
            }

            // last column
            if (this.getCells()[i][this.columns - 1].matchesOrBlank(die, ignoreColor, ignoreShade)) {
                edges.add(i * this.columns + this.columns - 1);
            }
        }

        for (int j = 1; j < this.columns - 1; j++) {
            // first row
            if (this.getCells()[0][j].matchesOrBlank(die, ignoreColor, ignoreShade)) {
                edges.add(j);
            }

            // last row
            if (this.getCells()[this.rows - 1][j].matchesOrBlank(die, ignoreColor, ignoreShade)) {
                edges.add((this.rows - 1) * this.columns + j);
            }
        }
        return edges;
    }

    private Integer orthogonalMatchingNeighbour(Die die, int location, int i, int j, boolean ignoreColor, boolean ignoreShade, boolean ignoreAjacency) {
        if ((!die.getColor().equals(this.cells[location / columns][location % columns].getColor()) &&
                !(die.getShade().equals(this.cells[location / columns][location % columns].getShade())))
                || ignoreAjacency) {
            return this.matchingNeighbour(die, i, j, ignoreColor, ignoreShade);
        }
        return null;
    }

    private Integer matchingNeighbour(Die die, int i, int j, boolean ignoreColor, boolean ignoreShade) {
        if (i < 0 || j < 0 || i > rows - 1 || j > columns - 1) {
            return null;
        }
        if(this.cells[i][j].matchesOrBlank(die, ignoreShade, ignoreColor)) {
            return i * columns + j;
        }
        return null;
    }

    private List<Integer> cellsAround(Die die, int location, boolean ignoreColor, boolean ignoreShade, boolean ignoreAdjacency) {

        int i = location / this.columns;
        int j = location % this.columns;

        List<Integer> around = new ArrayList<>(8);

        around.add(orthogonalMatchingNeighbour(die, location, i+1, j, ignoreColor, ignoreShade, ignoreAdjacency));
        around.add(orthogonalMatchingNeighbour(die, location, i-1, j, ignoreColor, ignoreShade, ignoreAdjacency));
        around.add(orthogonalMatchingNeighbour(die, location, i, j+1, ignoreColor, ignoreShade, ignoreAdjacency));
        around.add(orthogonalMatchingNeighbour(die, location, i, j-1, ignoreColor, ignoreShade, ignoreAdjacency));

        around.add(matchingNeighbour(die, i+1, j+1, ignoreColor, ignoreShade));
        around.add(matchingNeighbour(die, i-1, j+1, ignoreColor, ignoreShade));
        around.add(matchingNeighbour(die, i+1, j-1, ignoreColor, ignoreShade));
        around.add(matchingNeighbour(die, i-1, j-1, ignoreColor, ignoreShade));

        return around;
    }

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

        for (int location : this.getLocations()) {
            availablePositions.addAll(cellsAround(die, location, ignoreColor, ignoreShade, ignoreAdjacency));
        }
        availablePositions.removeAll(Collections.singleton(null));
        return availablePositions.stream().distinct().collect(Collectors.toList());
    }

}


