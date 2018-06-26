package it.polimi.ingsw.server.controllers;

import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.RoundTrack;

public class RoundTrackControllerImpl  {
    
    private final RoundTrack roundTrack;
    
    public RoundTrackControllerImpl(RoundTrack roundTrack) {
        this.roundTrack = roundTrack;
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
}
