package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WindowTest {

    private Window window;
    private Cell[][] cells;

    @BeforeEach
    void setUp() {

        this.cells = new Cell[3][4];

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j] = new Cell(0, null);
            }

        }

        cells[0][1] = new Cell(5, null);
        cells[0][4] = new Cell(0, GlassColor.GREEN);
        cells[1][2] = new Cell(2, null);
        cells[1][3] = new Cell(3, GlassColor.PURPLE);
        cells[2][0] = new Cell(0, GlassColor.BLUE);
        cells[2][1] = new Cell(2, null);
        cells[2][2] = new Cell(0, GlassColor.RED);
        cells[2][3] = new Cell(0, GlassColor.YELLOW);
        cells[2][4] = new Cell(3, null);
        cells[3][2] = new Cell(4, null);

        this.window = new Window(22, 5, 4, "test", null, cells);
    }
    @Test
    void getDifficulty() {
    }

    @Test
    void getId() {
    }

    @Test
    void getSibling() {
    }

    @Test
    void getCells() {
    }

    @Test
    void getPossiblePositionsForDie() {
    }

    @Test
    void putDie() {
    }

    @Test
    void getLocations() {
    }

    @Test
    void getDice() {
    }

    @Test
    void getFreeSpace() {
    }

    @Test
    void pickDie() {
    }

    @Test
    void pickDie1() {
    }

    @Test
    void getNumberOfDice() {
    }
}