package it.polimi.ingsw.net.responses;

import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

public class SignInResponse implements JSONSerializable {

    private static final long serialVersionUID = 1577258799621191622L;

    /**
     * The player's token.
     */
    @JSONElement("token")
    private String token;

    @JSONDesignatedConstructor
    public SignInResponse(@JSONElement("token") String token) {
        this.token = token;
    }

    /**
     * @return the player's toke
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the player's toke
     */
    public void setToken(String token) {
        this.token = token;
    }
}
