package it.polimi.ingsw.views;

import it.polimi.ingsw.controllers.RoundTrackController;
import it.polimi.ingsw.models.RoundTrack;

public abstract class RoundTrackView {

    protected RoundTrack roundTrack;
    protected RoundTrackController roundTrackController;

    public void setRoundTrackController(RoundTrackController roundTrackController) {
        this.roundTrackController = roundTrackController;
    }

    public void setRoundTrack(RoundTrack roundTrack) {
        this.roundTrack = roundTrack;
    }
}
