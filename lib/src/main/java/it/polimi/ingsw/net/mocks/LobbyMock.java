package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

import java.util.Arrays;

public class LobbyMock implements ILobby {

    private static final long serialVersionUID = -2248480612468786680L;

    private int id;
    private long openingTime;
    private long closingTime;
    private int timeRemaining;
    private IPlayer[] players;

    public LobbyMock(ILobby iLobby) {
        this(
                iLobby.getId(),
                iLobby.getOpeningTime(),
                iLobby.getClosingTime(),
                iLobby.getTimeRemaining(),
                Arrays.stream(iLobby.getPlayers())
                        .map(PlayerMock::new)
                        .toArray(PlayerMock[]::new)
        );
    }

    @JSONDesignatedConstructor
    public LobbyMock(
            @JSONElement("id") int id,
            @JSONElement("opening-time") long openingTime,
            @JSONElement("closing-time") long closingTime,
            @JSONElement("time-remaining") int timeRemaining,
            @JSONElement("players") PlayerMock[] players
    ) {
        this.id = id;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.timeRemaining = timeRemaining;
        this.players = players;
    }

    @Override
    @JSONElement("id")
    public int getId() {
        return this.id;
    }

    @Override
    @JSONElement("opening-time")
    public long getOpeningTime() {
        return this.openingTime;
    }

    @Override
    @JSONElement("closing-time")
    public long getClosingTime() {
        return this.closingTime;
    }

    @Override
    @JSONElement("time-remaining")
    public int getTimeRemaining() {
        return this.timeRemaining;
    }

    @Override
    @JSONElement("players")
    public IPlayer[] getPlayers() {
        return this.players;
    }
}
