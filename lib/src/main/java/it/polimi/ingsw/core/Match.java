package it.polimi.ingsw.core;

import it.polimi.ingsw.models.*;

public class Match {

    private static Match instance;
    private Player[] players;
    private Round[] rounds;
    private int currentRoundIndex;
    private RoundTrack roundTrack;
    private Bag bag;
    private DraftPool draftPool;
    private ObjectiveCard[] publicObjectiveCards;
    private ToolCard[] toolcards;

    public static Match getMatch(Player[] players, Round[] rounds, RoundTrack roundTrack, Bag bag, DraftPool draftPool, ObjectiveCard[] publicObjectiveCards, ToolCard[] toolcards) {
        if (Match.instance == null) {
            Match.instance = new Match(players, rounds, roundTrack, bag, draftPool, publicObjectiveCards, toolcards);
            return Match.instance;
        } else {
            return Match.instance;
        }
    }

    private Match(Player[] players, Round[] rounds, RoundTrack roundTrack, Bag bag, DraftPool draftPool, ObjectiveCard[] publicObjectiveCards, ToolCard[] toolcards) {
        this.players = players;
        this.rounds = rounds;
        this.roundTrack = roundTrack;
        this.bag = bag;
        this.draftPool = draftPool;
        this.publicObjectiveCards = publicObjectiveCards;
        this.toolcards = toolcards;
    }
}
