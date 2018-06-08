package it.polimi.ingsw.net.requests;

import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

public class SignUpRequest implements JSONSerializable {

    private static final long serialVersionUID = 3564382168491273076L;

    public static final String USERNAME_FIELD = "username";
    @SuppressWarnings("squid:S2068")
    public static final String PASSWORD_FIELD = "password";

    /**
     * The player's username.
     */
    private String username;

    /**
     * The player's password.
     */
    private String password;

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

    public SignUpRequest() { }

    public SignUpRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        this.username = jsonObject.getString(USERNAME_FIELD);
        this.password = jsonObject.getString(PASSWORD_FIELD);
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(USERNAME_FIELD, this.username);
        jsonObject.put(PASSWORD_FIELD, this.password);

        return jsonObject;
    }
}
