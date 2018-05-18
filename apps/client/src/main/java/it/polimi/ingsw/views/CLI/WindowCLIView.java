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

        char c='A';
        System.out.println("Window name:\t" + this.id);
        System.out.println("Difficulty:\t\t" + this.difficulty+"\n");

        for (int i = -1; i < rows ; i++) {
            for (int j = -1; j < columns; j++) {
                if (i == -1 && j == -1) {
                    System.out.print("  ");
                }
                else if (j == -1) {
                    System.out.print(c+" ");
                    c++;
                }
                else if (i == -1) {
                    System.out.print(" "+(j+1)+" ");
                }
                else cells[i][j].render();
            }
            System.out.println();
        }

    }
}
