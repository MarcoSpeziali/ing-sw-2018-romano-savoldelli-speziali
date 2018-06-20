package it.polimi.ingsw.views;

import it.polimi.ingsw.controllers.CellController;
import it.polimi.ingsw.listeners.OnDiePickedListener;
import it.polimi.ingsw.listeners.OnDiePutListener;
import it.polimi.ingsw.models.Cell;

public abstract class CellView {

    protected CellController cellController;
    protected Cell cell;

    public CellView() {
        //this.cell.addListener((OnDiePickedListener) (die, location) -> this.cell.pickDie());
        //this.cell.addListener((OnDiePutListener) (die, location) -> this.cell.putDie(die));
    }

    public void setCellController(CellController cellController) {
        this.cellController = cellController;
    }

    public void setCell(Cell cell) {
    }

}
