package it.polimi.ingsw.views;

import it.polimi.ingsw.controllers.RoundTrackController;
import it.polimi.ingsw.models.RoundTrack;

import java.io.IOException;

public abstract class RoundTrackView {

    protected RoundTrack roundTrack;
    protected RoundTrackController roundTrackController;

    public void setRoundTrackController(RoundTrackController roundTrackController) {
        this.roundTrackController = roundTrackController;
    }

    public void setRoundTrack(RoundTrack roundTrack) throws IOException {
        this.roundTrack = roundTrack;
    }
}
