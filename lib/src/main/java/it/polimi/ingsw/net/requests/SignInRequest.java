package it.polimi.ingsw.net.requests;

import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

public class SignInRequest implements JSONSerializable {

    private static final long serialVersionUID = 4772264309612799337L;

    /**
     * The player's username.
     */
    @JSONElement("username")
    private String username;

    @JSONDesignatedConstructor
    public SignInRequest(@JSONElement("username") String username) {
        this.username = username;
    }

    /**
     * @return the player's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the player's username
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
