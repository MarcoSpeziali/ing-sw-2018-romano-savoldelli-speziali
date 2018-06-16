package it.polimi.ingsw.net;

import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.net.utils.RequestFields;
import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

/**
 * Represents the header of the request or a response.
 */
public class Header implements JSONSerializable {
    private static final long serialVersionUID = -5858895441251305795L;

    /**
     * The user's token.
     */
    private String clientToken;

    /**
     * The endpoint function.
     */
    private EndPointFunction endPointFunction;

    public Header() {
    }

    public Header(String clientToken) {
        this(clientToken, null);
    }

    public Header(EndPointFunction endPointFunction) {
        this(null, endPointFunction);
    }

    public Header(String clientToken, EndPointFunction endPointFunction) {
        this.clientToken = clientToken;
        this.endPointFunction = endPointFunction;
    }

    /**
     * @return the user's token
     */
    public String getClientToken() {
        return clientToken;
    }

    /**
     * Sets the user's token.
     *
     * @param clientToken the user's token
     */
    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    /**
     * @return the endpoint function
     */
    public EndPointFunction getEndPointFunction() {
        return endPointFunction;
    }

    /**
     * @param endPointFunction the endpoint function
     */
    public void setEndPointFunction(EndPointFunction endPointFunction) {
        this.endPointFunction = endPointFunction;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        if (jsonObject.has(RequestFields.Header.CLIENT_TOKEN.toString())) {
            this.clientToken = jsonObject.getString(RequestFields.Header.CLIENT_TOKEN.toString());
        }

        if (jsonObject.has(RequestFields.Header.ENDPOINT.toString())) {
            this.endPointFunction = EndPointFunction.fromEndPointFunctionName(
                    jsonObject.getString(RequestFields.Header.ENDPOINT.toString())
            );
        }
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        if (this.clientToken != null) {
            jsonObject.put(
                    RequestFields.Header.CLIENT_TOKEN.toString(),
                    this.clientToken
            );
        }

        if (this.endPointFunction != null) {
            jsonObject.put(
                    RequestFields.Header.ENDPOINT.toString(),
                    this.endPointFunction.toString()
            );
        }

        return jsonObject;
    }

    @Override
    public String toString() {
        return this.serialize().toString();
    }
}
