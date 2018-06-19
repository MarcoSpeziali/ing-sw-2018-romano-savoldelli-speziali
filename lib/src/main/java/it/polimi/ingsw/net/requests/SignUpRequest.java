package it.polimi.ingsw.net.requests;

import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

public class SignUpRequest implements JSONSerializable {

    private static final long serialVersionUID = 3564382168491273076L;

    /**
     * The player's username.
     */
    @JSONElement("username")
    private String username;

    /**
     * The player's password.
     */
    @JSONElement("password")
    private String password;

    public SignUpRequest() {
    }

    @JSONDesignatedConstructor
    public SignUpRequest(
            @JSONElement("username") String username,
            @JSONElement("password") String password
    ) {
        this.username = username;
        this.password = password;
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

    /**
     * @return the player's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the player's password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
