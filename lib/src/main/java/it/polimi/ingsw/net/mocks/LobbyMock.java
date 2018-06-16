package it.polimi.ingsw.net.mocks;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class LobbyMock implements ILobby {

    private static final long serialVersionUID = -2248480612468786680L;

    private int id;
    private long openingTime;
    private long closingTime;
    private int timeRemaining;
    private List<IPlayer> players;

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public long getOpeningTime() {
        return this.openingTime;
    }

    @Override
    public long getClosingTime() {
        return this.closingTime;
    }

    @Override
    public int getTimeRemaining() {
        return this.timeRemaining;
    }

    @Override
    public List<IPlayer> getPlayers() {
        return this.players;
    }

    public LobbyMock() {}

    public LobbyMock(ILobby iLobby) {
        this.id = iLobby.getId();
        this.openingTime = iLobby.getOpeningTime();
        this.closingTime = iLobby.getClosingTime();
        this.timeRemaining = iLobby.getTimeRemaining();
        this.players = iLobby.getPlayers();
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        this.id = jsonObject.getInt("id");
        this.openingTime = jsonObject.getLong("opening-time");
        this.closingTime = jsonObject.getLong("closing-time");
        this.timeRemaining = jsonObject.getInt("time-remaining");

        JSONArray jsonArray = jsonObject.getJSONArray("players");

        this.players = new LinkedList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            PlayerMock playerMock = new PlayerMock();
            playerMock.deserialize(jsonArray.getJSONObject(i));

            this.players.add(playerMock);
        }
    }
}
