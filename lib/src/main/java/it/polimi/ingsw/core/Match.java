package it.polimi.ingsw.core;

import it.polimi.ingsw.models.*;

import java.io.Serializable;

public class Match implements Serializable {

    private static final long serialVersionUID = 53082781936982168L;

    private Player[] players;

    private Round[] rounds;
    private int currentRoundIndex;
    private RoundTrack roundTrack;
    private Bag bag;
    private DraftPool draftPool;
    private ObjectiveCard[] publicObjectiveCards;

    private ToolCard[] toolCards;

    private Match(Player[] players, Round[] rounds, RoundTrack roundTrack, Bag bag, DraftPool draftPool, ObjectiveCard[] publicObjectiveCards, ToolCard[] toolCards) {
        this.players = players;
        this.rounds = rounds;
        this.roundTrack = roundTrack;
        this.bag = bag;
        this.draftPool = draftPool;
        this.publicObjectiveCards = publicObjectiveCards;
        this.toolCards = toolCards;
    }

    public int getNumberOfPlayer() {
        return this.players.length;
    }
}
