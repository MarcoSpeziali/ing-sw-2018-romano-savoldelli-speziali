package it.polimi.ingsw.server.controllers;

import it.polimi.ingsw.controllers.DieInteractionException;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.models.Die;

public class CellControllerImpl {

    private final Cell cell;

    public CellControllerImpl(Cell cell) {
        this.cell = cell;
    }

    public Cell getCell() {
        return this.cell;
    }

    public Die tryToPick() {
        return this.cell.pickDie();
    }

    public void tryToPut(Die die, boolean ignoreColor, boolean ignoreShade) throws DieInteractionException {
        if (this.cell.matchesOrBlank(die, ignoreColor, ignoreShade)) {
            this.cell.putDie(die);
        }

        throw new DieInteractionException();
    }
}
