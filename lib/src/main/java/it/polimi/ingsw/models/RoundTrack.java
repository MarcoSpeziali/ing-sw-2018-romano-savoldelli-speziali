package it.polimi.ingsw.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RoundTrack {

    private List<Die> rounds;
    private int currentRound;

    /**
     * Sets up a new {@link RoundTrack} of a specified number of rounds.
     * @param round the number of rounds.
     */
    public RoundTrack(int round) {
        this.rounds = new LinkedList<>();
        this.currentRound = 0;
    }

    /**
     * @param round the round of which return the {@link Die}.
     * @return the assigned {@link Die} of a specified round.
     * @throws ArrayIndexOutOfBoundsException if {@link Die} is not present.
     */
    public Die getDieAtIndex(int round) throws ArrayIndexOutOfBoundsException {
        return this.rounds.get(round);
    }

    /**
     * @param die an instance of {@link Die} that must be set for current round.
     */
    public void setDieForCurrentRound(Die die) {
        this.rounds.set(this.currentRound, die);
        this.incrementRound();
    }

    /**
     * Increments the current round.
     */
    private void incrementRound() {
        this.currentRound++;
    }
 }
