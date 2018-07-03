package it.polimi.ingsw.server.controllers;

import it.polimi.ingsw.controllers.DieInteractionException;
import it.polimi.ingsw.core.locations.FullLocationException;
import it.polimi.ingsw.listeners.OnDiePickedListener;
import it.polimi.ingsw.listeners.OnDiePutListener;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.models.Die;

public class CellControllerImpl implements OnDiePickedListener, OnDiePutListener {
    
    private static final long serialVersionUID = -4168507382863447304L;
    
    private Cell cell;
    private Cell cellUpdate;
    private final transient Object syncObject = new Object();

    public CellControllerImpl(Cell cell) {
        this.cell = cell;
        this.cell.addOnDiePutListener(this);
        this.cell.addOnDiePickedListener(this);
    }

    public Cell getCell() {
        return this.cell;
    }

    public Die tryToPick() {
        return this.cell.pickDie();
    }

    public void tryToPut(Die die, boolean ignoreColor, boolean ignoreShade) throws DieInteractionException {
        if (this.cell.matchesOrBlank(die, ignoreColor, ignoreShade)) {
            try {
                this.cell.putDie(die);
            }
            catch (FullLocationException e) {
                throw new DieInteractionException();
            }

            return;
        }

        throw new DieInteractionException();
    }
    
    public Cell waitForUpdate() throws InterruptedException {
        synchronized (syncObject) {
            while (cellUpdate == null) {
                syncObject.wait();
            }
            
            Cell update = cellUpdate;
            this.cell = update;
            this.cellUpdate = null;
            
            return update;
        }
    }
    
    @Override
    public void onDiePicked(Die die, Integer location) {
        synchronized (syncObject) {
            cellUpdate = this.getCell();
            syncObject.notifyAll();
        }
    }
    
    @Override
    public void onDiePut(Die die, Integer location) {
        synchronized (syncObject) {
            cellUpdate = this.getCell();
            syncObject.notifyAll();
        }
    }
}
