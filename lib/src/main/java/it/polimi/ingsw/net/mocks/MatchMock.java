package it.polimi.ingsw.net.mocks;

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

    public MatchMock(IMatch iMatch) {
        this(
                iMatch.getId(),
                iMatch.getStartingTime(),
                iMatch.getEndingTime(),
                new LobbyMock(iMatch.getLobby()),
                Arrays.stream(iMatch.getPlayers())
                        .map(LivePlayerMock::new)
                        .toArray(LivePlayerMock[]::new)
        );
    }

    @JSONDesignatedConstructor
    public MatchMock(
            @JSONElement("id") int id,
            @JSONElement("starting-time") long startingTime,
            @JSONElement("ending-time") long endingTime,
            @JSONElement("lobby") LobbyMock lobby,
            @JSONElement("players") LivePlayerMock[] players
    ) {
        this.id = id;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.lobby = lobby;
        this.players = players;
    }
    
    public MatchMock(IMatch iMatch, ILivePlayer[] players) {
        this(
                iMatch.getId(),
                iMatch.getStartingTime(),
                iMatch.getEndingTime(),
                new LobbyMock(iMatch.getLobby()),
                Arrays.stream(players)
                        .map(LivePlayerMock::new)
                        .toArray(LivePlayerMock[]::new)
        );
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
    public IDraftPool getDraftPool() {
        return null;
    }
    
    @Override
    public IRoundTrack getRoundTrack() {
        return null;
    }
    
    @Override
    public IObjectiveCard[] getObjectiveCards() {
        return new IObjectiveCard[0];
    }
    
    @Override
    public IToolCard[] getToolCards() {
        return new IToolCard[0];
    }
}
