package it.polimi.ingsw.controllers;

import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.models.Die;

public class CellController {

    private Cell cell;

    public CellController(Cell cell) {
        this.cell = cell;
    }
    public void setCellModel(Cell cell) {
        this.cell = cell;
    }

    public Die onDiePicked() {
        return this.cell.pickDie();
    }

    public void onDiePut(Die die) {
        this.cell.putDie(die);
    }

}
