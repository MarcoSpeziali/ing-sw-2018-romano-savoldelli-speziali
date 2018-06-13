package it.polimi.ingsw.net.mocks;

import org.json.JSONObject;

public class PlayerMock implements IPlayer {

    private static final long serialVersionUID = -3331911603803436043L;

    private int id;
    private String username;

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        this.id = jsonObject.getInt("id");
        this.username = jsonObject.getString("username");
    }
}
