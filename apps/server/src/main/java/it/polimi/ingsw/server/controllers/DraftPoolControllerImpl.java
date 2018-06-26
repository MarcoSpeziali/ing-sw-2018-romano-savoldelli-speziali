package it.polimi.ingsw.server.controllers;

import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.DraftPool;

public class DraftPoolControllerImpl {
    
    private final DraftPool draftPool;
    
    public DraftPoolControllerImpl(DraftPool draftPool) {
        this.draftPool = draftPool;
    }
    
    public DraftPool getDraftPool() {
        return this.draftPool;
    }
    
    public Die tryToPick(Die die) {
        return this.draftPool.pickDie(die);
    }
    
    public Die tryToPick(Integer location) {
        return this.draftPool.pickDie(location);
    }
    
    public void tryToPut(Die die) {
        this.draftPool.putDie(die);
    }
}
