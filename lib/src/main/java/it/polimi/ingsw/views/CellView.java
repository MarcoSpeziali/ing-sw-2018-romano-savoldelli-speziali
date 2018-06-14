package it.polimi.ingsw.views;

import it.polimi.ingsw.controllers.CellController;
import it.polimi.ingsw.models.Cell;
import javafx.scene.image.ImageView;

public abstract class CellView {

    protected CellController cellController;
    protected Cell cell;

    public CellView(Cell cell) {
        this.cell = cell;
    }

    public void setCellController(CellController cellController) {
        this.cellController = cellController;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

}
