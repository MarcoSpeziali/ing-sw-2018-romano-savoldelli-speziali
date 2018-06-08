package it.polimi.ingsw.net.requests;

import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

public class SignInRequest implements JSONSerializable {

    private static final long serialVersionUID = 4772264309612799337L;

    public static final String USERNAME_FIELD = "username";

    /**
     * The player's username.
     */
    private String username;

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

    public SignInRequest() { }

    public SignInRequest(String username) {
        this.username = username;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        this.username = jsonObject.getString(USERNAME_FIELD);
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(USERNAME_FIELD, this.username);

        return jsonObject;
    }
}
