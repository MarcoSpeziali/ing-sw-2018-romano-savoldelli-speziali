package it.polimi.ingsw.server.controllers;

import it.polimi.ingsw.listeners.OnDiePickedListener;
import it.polimi.ingsw.listeners.OnDiePutListener;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.DraftPool;
import it.polimi.ingsw.net.mocks.IDie;
import it.polimi.ingsw.server.events.EventDispatcher;
import it.polimi.ingsw.server.events.EventType;
import it.polimi.ingsw.server.events.ModelUpdateListener;

public class DraftPoolControllerImpl implements OnDiePickedListener, OnDiePutListener {

    private static final long serialVersionUID = 2732485027634982507L;

    private final DraftPool draftPool;
    
    public DraftPoolControllerImpl(DraftPool draftPool) {
        this.draftPool = draftPool;
        this.draftPool.addPickListener(this);
        this.draftPool.addPutListener(this);
    }
    
    public DraftPool getDraftPool() {
        return this.draftPool;
    }

    public IDie getDieAtLocation(Integer location) {
        return this.draftPool.getLocationDieMap().getOrDefault(location, null);
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

    @Override
    public void onDiePicked(Die die, Integer location) {
        EventDispatcher.dispatch(
                EventType.MODEL_UPDATES,
                ModelUpdateListener.class,
                modelUpdateListener -> modelUpdateListener.onModelUpdated(this, this.draftPool)
        );
    }

    @Override
    public void onDiePut(Die die, Integer location) {
        EventDispatcher.dispatch(
                EventType.MODEL_UPDATES,
                ModelUpdateListener.class,
                modelUpdateListener -> modelUpdateListener.onModelUpdated(this, this.draftPool)
        );
    }
}
