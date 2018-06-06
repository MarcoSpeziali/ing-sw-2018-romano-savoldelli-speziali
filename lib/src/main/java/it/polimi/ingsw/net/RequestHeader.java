package it.polimi.ingsw.net;

import it.polimi.ingsw.net.utils.RequestFields;
import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

/**
 * Represents the header of the request.
 */
public class RequestHeader implements JSONSerializable {
    private static final long serialVersionUID = -5858895441251305795L;

    /**
     * The user's token.
     */
    private String clientToken;

    /**
     * @return the user's token
     */
    public String getClientToken() {
        return clientToken;
    }

    /**
     * Sets the user's token.
     * @param clientToken the user's token
     */
    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public RequestHeader() { }

    public RequestHeader(String clientToken) {
        this.clientToken = clientToken;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        if (jsonObject.has(RequestFields.Header.CLIENT_TOKEN.toString())) {
            this.clientToken = jsonObject.getString(RequestFields.Header.CLIENT_TOKEN.toString());
        }
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(
                RequestFields.Header.CLIENT_TOKEN.toString(),
                this.clientToken
        );

        return jsonObject;
    }

    @Override
    public String toString() {
        return this.serialize().toString();
    }
}
