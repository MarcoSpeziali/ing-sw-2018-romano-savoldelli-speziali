package it.polimi.ingsw.models;

public class Window {

    private int difficulty;
    private int rows;
    private int columns;
    private String id;
    private Window sibling;
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
}
