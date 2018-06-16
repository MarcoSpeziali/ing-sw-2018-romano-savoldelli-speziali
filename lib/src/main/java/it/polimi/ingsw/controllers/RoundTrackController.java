package it.polimi.ingsw.controllers;

import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.RoundTrack;

import java.io.Serializable;

public class RoundTrackController implements Serializable {
    private static final long serialVersionUID = -9067002400585127131L;

    private RoundTrack roundTrack;

    public RoundTrackController(RoundTrack roundTrack) {
        this.setRoundTrackModel(roundTrack);
    }

    public void setRoundTrackModel(RoundTrack roundTrack) {
        this.roundTrack = roundTrack;
    }

    public void putDieForRound(Die die, int round) {
        this.putDieForRoundAtIndex(die, round, RoundTrack.MAX_NUMBER_OF_DICE_PER_ROUND);
    }

    public void putDieForRoundAtIndex(Die die, int round, int index) {
        this.roundTrack.setDieForRoundAtIndex(die, round, index);
    }
}
