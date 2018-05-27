package it.polimi.ingsw.views;

import it.polimi.ingsw.controllers.CellController;
import it.polimi.ingsw.models.Cell;

public abstract class CellView implements Renderable {

    protected CellController cellController;
    protected Cell cell;

    public void setCellController(CellController cellController) {
        this.cellController = cellController;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }
}
