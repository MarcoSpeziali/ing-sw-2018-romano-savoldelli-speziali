package it.polimi.ingsw.views.CLI;

import it.polimi.ingsw.views.WindowsView;

public class WindowCLIView extends WindowsView {

    private int rows;
    private int columns;
    private int difficulty;
    private String id;
    private CellCLIView[][] cells;


    public WindowCLIView(int rows, int columns, int difficulty, String id, CellCLIView[][] cells) {
        this.rows = rows;
        this.columns = columns;
        this.difficulty = difficulty;
        this.id = id;

        this.cells = cells;
    }


    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getId() {
        return id;
    }

    public CellCLIView[][] getCells() {
        return cells;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCells(CellCLIView[][] cells) {
        this.cells = cells;
    }

    @Override
    public void render() {

        System.out.println("Name: " + this.id + " Difficulty: " + this.difficulty + "\n");
        for (int i = 0; i < rows ; i++) {
            for (int j = 0; j < columns; j++) {
                cells[i][j].render();
            }
            System.out.println();
        }

    }
}
