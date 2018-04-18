package it.polimi.ingsw.core;

import it.polimi.ingsw.models.ObjectiveCard;
import it.polimi.ingsw.models.ToolCard;

public class Match {

    private static Match instance;
    private Players[] players;
    private Round[] rounds;
    private int currentRoundIndex;
    private RoundTrack roundTrack;
    private Bag bag;
    private DraftPool draftPool;
    private ObjectiveCard[] publicObjectiveCards;
    private ToolCard[] toolcards;

    private Match(Players[] players, Round[] rounds, RoundTrack roundTrack, Bag bag, DraftPool draftPool, ObjectiveCard[] publicObjectiveCards, ToolCard[] toolcards) {
        this.players = players;
        this.rounds = rounds;
        this.roundTrack = roundTrack;
        this.bag = bag;
        this.draftPool = draftPool;
        this.publicObjectiveCards = publicObjectiveCards;
        this.toolcards = toolcards;
    }

    public Match getMatch(Players[] players, Round[] rounds, RoundTrack roundTrack, Bag bag, DraftPool draftPool, ObjectiveCard[] publicObjectiveCards, ToolCard[] toolcards) {
        if (this.instance == null) {
            return new Match(players, rounds, roundTrack, bag, draftPool, publicObjectiveCards, toolcards);
        } else {
            return instance;
        }
    }
}
