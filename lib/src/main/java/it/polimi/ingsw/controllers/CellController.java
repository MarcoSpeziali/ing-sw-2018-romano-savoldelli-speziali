package it.polimi.ingsw.controllers;

import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.views.CellView;

public class CellController {

    private final CellView cellView;
    private final Cell cell;

    public CellController(CellView cellView, Cell cell) {
        this.cellView = cellView;
        this.cell = cell;

        cellView.onDiePut(die -> {
            this.cell.putDie(die);
        });

        cellView.onDiePicked(() -> {
             return this.cell.pickDie();
        });
    }

    public void onDiePut(Die die) {
        this.cell.putDie(die);
    }

    public Die onDiePicked() {
        return this.cell.pickDie();
    }
}
