package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class PlayerMock implements IPlayer {

    private static final long serialVersionUID = -3331911603803436043L;

    private int id;
    private String username;

    public PlayerMock(IPlayer iPlayer) {
        this(iPlayer.getId(), iPlayer.getUsername());
    }

    @JSONDesignatedConstructor
    public PlayerMock(
            @JSONElement("id") int id,
            @JSONElement("username") String username
    ) {
        this.id = id;
        this.username = username;
    }

    @Override
    @JSONElement("id")
    public int getId() {
        return this.id;
    }

    @Override
    @JSONElement("username")
    public String getUsername() {
        return this.username;
    }
}
