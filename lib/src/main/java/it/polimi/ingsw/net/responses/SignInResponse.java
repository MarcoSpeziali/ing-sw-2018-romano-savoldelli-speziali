package it.polimi.ingsw.net.responses;

import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

public class SignInResponse implements JSONSerializable {

    public static final String TOKEN_FIELD = "token";
    private static final long serialVersionUID = 1577258799621191622L;
    /**
     * The player's token.
     */
    private String token;

    public SignInResponse() {
    }

    public SignInResponse(String token) {
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

    @Override
    public void deserialize(JSONObject jsonObject) {
        this.token = jsonObject.getString(TOKEN_FIELD);
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(TOKEN_FIELD, this.token);

        return jsonObject;
    }
}
