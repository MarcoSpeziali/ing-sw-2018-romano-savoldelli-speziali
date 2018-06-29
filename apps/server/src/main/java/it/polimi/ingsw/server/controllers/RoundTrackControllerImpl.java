package it.polimi.ingsw.server.controllers;

import it.polimi.ingsw.listeners.OnDiePickedListener;
import it.polimi.ingsw.listeners.OnDiePutListener;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.RoundTrack;
import it.polimi.ingsw.server.events.EventDispatcher;
import it.polimi.ingsw.server.events.EventType;
import it.polimi.ingsw.server.events.ModelUpdateListener;

public class RoundTrackControllerImpl implements OnDiePutListener, OnDiePickedListener {

    private static final long serialVersionUID = 3848672171116625479L;

    private final RoundTrack roundTrack;
    
    public RoundTrackControllerImpl(RoundTrack roundTrack) {
        this.roundTrack = roundTrack;
        this.roundTrack.addPickListener(this);
        this.roundTrack.addPutListener(this);
    }
    
    public RoundTrack getRoundTrack() {
        return this.roundTrack;
    }
    
    public Die tryToPick(Die die) {
        return this.roundTrack.pickDie(die);
    }
    
    public Die tryToPick(Integer location) {
        return this.roundTrack.pickDie(location);
    }
    
    public void tryToPut(Die die, Integer location) {
        this.roundTrack.putDie(die, location);
    }

    @Override
    public void onDiePicked(Die die, Integer location) {
        EventDispatcher.dispatch(
                EventType.MODEL_UPDATES,
                ModelUpdateListener.class,
                modelUpdateListener -> modelUpdateListener.onModelUpdated(this, this.roundTrack)
        );
    }

    @Override
    public void onDiePut(Die die, Integer location) {
        EventDispatcher.dispatch(
                EventType.MODEL_UPDATES,
                ModelUpdateListener.class,
                modelUpdateListener -> modelUpdateListener.onModelUpdated(this, this.roundTrack)
        );
    }
}
