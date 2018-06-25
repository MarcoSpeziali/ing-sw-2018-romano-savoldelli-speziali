package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

import java.util.Objects;

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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlayerMock that = (PlayerMock) o;
        return id == that.id;
    }
    
    @Override
    public int hashCode() {
        
        return Objects.hash(id);
    }
}
