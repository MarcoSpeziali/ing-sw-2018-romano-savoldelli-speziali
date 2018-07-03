package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.core.Player;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

import java.util.Arrays;

public class MatchMock implements IMatch {

    private static final long serialVersionUID = -1540260126475214993L;

    private final int id;
    private final long startingTime;
    private final long endingTime;
    private final LobbyMock lobby;
    private final LivePlayerMock[] players;
    private final IDraftPool draftPool;
    private final IRoundTrack roundTrack;
    private final IObjectiveCard[] publicObjectiveCards;
    private final IToolCard[] toolCards;
    private final IObjectiveCard privateObjective;
    private final Player currentPlayer;

    public MatchMock(IMatch iMatch) {
        this(
                iMatch.getId(),
                iMatch.getStartingTime(),
                iMatch.getEndingTime(),
                new LobbyMock(iMatch.getLobby()),
                Arrays.stream(iMatch.getPlayers())
                        .map(LivePlayerMock::new)
                        .toArray(LivePlayerMock[]::new),
                new DraftPoolMock(iMatch.getDraftPool()),
                new RoundTrackMock(iMatch.getRoundTrack()),
                Arrays.stream(iMatch.getPublicObjectiveCards())
                        .map(ObjectiveCardMock::new)
                        .toArray(ObjectiveCardMock[]::new),
                Arrays.stream(iMatch.getToolCards())
                        .map(ToolCardMock::new)
                        .toArray(ToolCardMock[]::new),
                new ObjectiveCardMock(iMatch.getPrivateObjectiveCard()),
                iMatch.getCurrentPlayer()
        );
    }

    @JSONDesignatedConstructor
    public MatchMock(
            @JSONElement("id") int id,
            @JSONElement("starting-time") long startingTime,
            @JSONElement("ending-time") long endingTime,
            @JSONElement("lobby") LobbyMock lobby,
            @JSONElement("players") LivePlayerMock[] players,
            @JSONElement("draft-pool") DraftPoolMock draftPool,
            @JSONElement("round-track") RoundTrackMock roundTrack,
            @JSONElement("public-objective-cards") ObjectiveCardMock[] publicObjectiveCards,
            @JSONElement("tool-cards") ToolCardMock[] toolCards,
            @JSONElement("private-objective-card") ObjectiveCardMock privateObjective,
            @JSONElement("current-player") Player currentPlayer
    ) {
        this.id = id;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.lobby = lobby;
        this.players = players;
        this.draftPool = draftPool;
        this.roundTrack = roundTrack;
        this.publicObjectiveCards = publicObjectiveCards;
        this.toolCards = toolCards;
        this.privateObjective = privateObjective;
        this.currentPlayer = currentPlayer;
    }

    @Override
    @JSONElement("id")
    public int getId() {
        return id;
    }

    @Override
    @JSONElement("starting-time")
    public long getStartingTime() {
        return startingTime;
    }

    @Override
    @JSONElement("ending-time")
    public long getEndingTime() {
        return endingTime;
    }

    @Override
    @JSONElement("lobby")
    public ILobby getLobby() {
        return lobby;
    }

    @Override
    @JSONElement("players")
    public LivePlayerMock[] getPlayers() {
        return players;
    }
    
    @Override
    @JSONElement("draft-pool")
    public IDraftPool getDraftPool() {
        return draftPool;
    }
    
    @Override
    @JSONElement("round-track")
    public IRoundTrack getRoundTrack() {
        return roundTrack;
    }
    
    @Override
    @JSONElement("public-objective-cards")
    public IObjectiveCard[] getPublicObjectiveCards() {
        return publicObjectiveCards;
    }
    
    @Override
    @JSONElement("tool-cards")
    public IToolCard[] getToolCards() {
        return toolCards;
    }
    
    @Override
    @JSONElement("private-objective-card")
    public IObjectiveCard getPrivateObjectiveCard() {
        return privateObjective;
    }

    @Override
    @JSONElement("current-player")
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
