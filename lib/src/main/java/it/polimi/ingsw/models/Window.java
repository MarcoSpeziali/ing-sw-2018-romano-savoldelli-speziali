package it.polimi.ingsw.models;

import it.polimi.ingsw.core.locations.ChoosablePickLocation;
import it.polimi.ingsw.core.locations.RestrictedChoosablePutLocation;
import it.polimi.ingsw.listeners.OnDiePickedListener;
import it.polimi.ingsw.listeners.OnDiePutListener;
import it.polimi.ingsw.net.mocks.IWindow;
import it.polimi.ingsw.utils.IterableRange;

import java.util.*;
import java.util.stream.Collectors;

public class Window implements RestrictedChoosablePutLocation, ChoosablePickLocation, IWindow {

    private static final long serialVersionUID = 733952479357429786L;

    private String id;
    private int difficulty;
    private int rows;
    private int columns;
    // A multi-dimensional array cannot be serialized
    private Cell[][] cells;

    private transient List<OnDiePutListener> onDiePutListeners;
    private transient List<OnDiePickedListener> onDiePickedListeners;

    /**
     * Sets up a new {@link Window} with specified parameters.
     *
     * @param difficulty an integer representing the difficulty of the Windows.
     * @param rows       the number of rows.
     * @param columns    the number of columns.
     * @param id         a unique {@link String} representing the name of the Window.
     * @param cells      a matrix representing the disposition of dice.
     */
    public Window(int difficulty, int rows, int columns, String id, Cell[][] cells) {
        this.difficulty = difficulty;
        this.rows = rows;
        this.columns = columns;
        this.id = id;
        this.cells = cells;

        onDiePutListeners = new LinkedList<>();
        onDiePickedListeners = new LinkedList<>();
    }

    /**
     * @return an {@link Integer} representing the difficulty of the window.
     */
    public int getDifficulty() {
        return this.difficulty;
    }

    /**
     * @return a {@link String} representing the name of the window.
     */
    public String getId() {
        return this.id;
    }

    /**
     * @return the cells in the window
     */
    @Override
    public Cell[][] getCells() {
        return this.cells;
    }

    /**
     * @return the number of rows.
     */
    @Override
    public int getRows() {
        return rows;
    }

    /**
     * @return the number of columns.
     */
    @Override
    public int getColumns() {
        return columns;
    }

    @Override
    public Cell[] getFlatCells() {
        return Arrays.stream(this.cells)
                .flatMap(Arrays::stream)
                .toArray(Cell[]::new);
    }

    /**
     * Adds the die in a specified location.
     *
     * @param die      the {@link Die} that must be put into.
     * @param location the target location stretched in a one-dimensional array.
     */
    @Override
    public void putDie(Die die, Integer location) {
        this.cells[location / this.columns][location % this.columns].putDie(die);
        this.onDiePutListeners.forEach(onDiePutListener
                -> onDiePutListener.onDiePut(die, location));
    }

    /**
     * @return an instance of {@link List} of occupied locations.
     */
    @Override
    public List<Integer> getFullLocations() {
        List<Integer> locations = new LinkedList<>();

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                if (this.cells[i][j].isOccupied()) {
                    locations.add(i * this.columns + j);
                }
            }
        }

        return locations;
    }

    @Override
    public List<Integer> getEmptyLocations() {
        List<Integer> locations = new LinkedList<>();

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                if (!this.cells[i][j].isOccupied()) {
                    locations.add(i * this.columns + j);
                }
            }
        }

        return locations;
    }

    /**
     * @return an instance of {@link List} representing the two-dimensional matrix of cells.
     */
    @Override
    public List<Die> getDice() {
        return Arrays.stream(this.cells)
                .flatMap(Arrays::stream)
                .map(Cell::getDie)
                .collect(Collectors.toList());
    }

    /**
     * @return the size of the unoccupied space in cells.
     */
    @Override
    public int getFreeSpace() {
        return Math.toIntExact(Arrays.stream(this.cells)
                .flatMap(Arrays::stream)
                .filter(cell -> !cell.isOccupied())
                .count());
    }

    /**
     * Removes from cells and returns the same instance of the parameter, if present.
     *
     * @param die the instance of {@link Die} than must be compared to the target.
     * @return an instance of {@link Die}
     */
    @Override
    public Die pickDie(Die die) {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                if (die == this.cells[i][j].getDie()) {
                    return this.cells[i][j].pickDie();
                }
            }
        }
        this.onDiePickedListeners.forEach(onDiePickedListener
                -> onDiePickedListener.onDiePicked(die));
        return null;
    }

    /**
     * Removes from cells and returns a die of a specified location.
     *
     * @param location the one-dimensional index of the die in cells.
     * @return an instance of {@link Die}.
     */
    @Override
    public Die pickDie(Integer location) {
        Die die = this.cells[location / this.columns][location % this.columns].pickDie();
        this.onDiePickedListeners.forEach(onDiePickedListener -> onDiePickedListener.onDiePicked(die, location));
        return die;
    }

    @Override
    public Die getDie(Integer location) {
        return this.cells[location / this.columns][location % this.columns].getDie();
    }

    /**
     * @return the size of the occupied space in cells.
     */
    @Override
    public int getNumberOfDice() {
        return this.rows * this.columns - getFreeSpace();
    }

    /**
     * Check which initial locations are allowed in the window for a spcified die.
     *
     * @param die             the target {@link Die} which must be put.
     * @param ignoreColor     the boolean flag which avoids color control.
     * @param ignoreShade     the boolean flag which avoids shade control.
     * @param ignoreAdjacency the boolean flag which avoid adjacency control.
     * @return an instance of {@link List} of allowed locations.
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

    /**
     * Checks if color and shade similarity rules of adjacent orthogonal dice are respected, then inspects a specified cell.
     *
     * @param die         the {@link Die} which cell's color and shade must be compared with.
     * @param location    the previous one-dimensional index of the die in cells.
     * @param i           the current row index of the {@link Die} in cells.
     * @param j           the current column index of the {@link Die} in cells.
     * @param ignoreColor the boolean flag which avoids color control.
     * @param ignoreShade the boolean flag which avoids shade control.
     * @return a call of {@link #neighbour} method if control passes, null if it fails or cell is not occupied.
     */
    private Integer orthogonalNeighbour(Die die, int location, int i, int j, boolean ignoreColor, boolean ignoreShade) {

        Die d = this.cells[location / this.columns][location % this.columns].getDie();
        if (d == null) {
            return null;
        }

        if (!die.getColor().equals(d.getColor()) && !(die.getShade().equals(d.getShade()))) {
            return this.neighbour(die, i, j, ignoreColor, ignoreShade);
        }
        return null;
    }

    /**
     * Inspects a specified cell, verifying its correct matching of color and shade with a target {@link Die}.
     *
     * @param die         the {@link Die} which cell's color and shade must be compared with.
     * @param i           the row index of the {@link Die} in cells.
     * @param j           the column index of the {@link Die} in cells.
     * @param ignoreColor the boolean flag which avoids color control.
     * @param ignoreShade the boolean flag which avoids shade control.
     * @return the index of neighbour if control passes, null if it fails or index is out of bounds.
     */
    private Integer neighbour(Die die, int i, int j, boolean ignoreColor, boolean ignoreShade) {
        if (i < 0 || j < 0 || i > this.rows - 1 || j > this.columns - 1) {
            return null;
        }

        if (this.cells[i][j].matchesOrBlank(die, ignoreShade, ignoreColor)) {
            return i * this.columns + j;
        }
        return null;
    }

    /**
     * Inspect the surrounding cells of a target one, referring to the location parameter.
     *
     * @param die         a {@link Die} which must be compared with cells around.
     * @param location    the one-dimensional index of the die in cells.
     * @param ignoreColor the boolean flag which avoids color control.
     * @param ignoreShade the boolean flag which avoids shade control.
     * @return an instance of {@link List} of matching indexes.
     */
    private List<Integer> cellsAround(Die die, int location, boolean ignoreColor, boolean ignoreShade) {

        int i = location / this.columns;
        int j = location % this.columns;

        List<Integer> surrounding = new ArrayList<>(8);

        surrounding.add(orthogonalNeighbour(die, location, i + 1, j, ignoreColor, ignoreShade));
        surrounding.add(orthogonalNeighbour(die, location, i - 1, j, ignoreColor, ignoreShade));
        surrounding.add(orthogonalNeighbour(die, location, i, j + 1, ignoreColor, ignoreShade));
        surrounding.add(orthogonalNeighbour(die, location, i, j - 1, ignoreColor, ignoreShade));

        surrounding.add(neighbour(die, i + 1, j + 1, ignoreColor, ignoreShade));
        surrounding.add(neighbour(die, i - 1, j + 1, ignoreColor, ignoreShade));
        surrounding.add(neighbour(die, i + 1, j - 1, ignoreColor, ignoreShade));
        surrounding.add(neighbour(die, i - 1, j - 1, ignoreColor, ignoreShade));

        return surrounding;
    }

    /**
     * Checks which locations are allowed in the window for a specified die.
     *
     * @param die             the target {@link Die} which must be put.
     * @param ignoreColor     the boolean flag which avoids color control.
     * @param ignoreShade     the boolean flag which avoids shade control.
     * @param ignoreAdjacency the boolean flag which avoid adjacency control.
     * @return an instance a {@link List} of allowed locations.
     */
    @Override
    public List<Integer> getPossiblePositionsForDie(Die die, Boolean ignoreColor, Boolean ignoreShade, Boolean ignoreAdjacency) {
        List<Integer> availablePositions = new ArrayList<>(this.rows * this.columns);

        if (die.getShade() == 0 || die.getColor() == null) {
            throw new IllegalArgumentException("Die's shade and color must both be set!");
        }

        if (this.getFullLocations().isEmpty()) {
            return this.getEdgesPositions(die, ignoreColor, ignoreShade, ignoreAdjacency);
        }

        if (ignoreAdjacency) {
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.columns; j++) {
                    availablePositions.addAll(cellsAround(die, i * this.columns + j, ignoreColor, ignoreShade));
                }
            }
        }

        else {
            for (int location : this.getFullLocations()) {
                availablePositions.addAll(cellsAround(die, location, ignoreColor, ignoreShade));
            }
        }

        availablePositions.removeAll(Collections.singleton(null));
        return availablePositions.stream()
                .distinct()
                .filter(location -> !this.cells[location / this.columns][location % this.columns].isOccupied())
                .collect(Collectors.toList());
    }
    
    public void addPutListener(OnDiePutListener onDiePutListener) {
        if (this.onDiePutListeners == null) {
            this.onDiePutListeners = new LinkedList<>();
        }

        this.onDiePutListeners.add(onDiePutListener);
    }
    
    public void addPickListener(OnDiePickedListener onDiePickedListener) {
        if (this.onDiePickedListeners == null) {
            this.onDiePickedListeners = new LinkedList<>();
        }

        this.onDiePickedListeners.add(onDiePickedListener);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Window window = (Window) o;
        return Objects.equals(id, window.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


