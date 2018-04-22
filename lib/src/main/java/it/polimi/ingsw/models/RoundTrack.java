package it.polimi.ingsw.models;

import it.polimi.ingsw.core.Die;

public class RoundTrack {

    private Die[] rounds;
    private int currentRound;

    public RoundTrack() {
        this.rounds = new Die[10];
        this.currentRound = 0;
    }

    public Die getDieAtIndex(int index) throws ArrayIndexOutOfBoundsException {
        return this.rounds[index];
    }

    public void setDieForCurrentRound(Die die) {
        this.rounds[this.currentRound] = die;
        this.incrementRound();
    }

    private void incrementRound() {
        this.currentRound++;
    }
 }
