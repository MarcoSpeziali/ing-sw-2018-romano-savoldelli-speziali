package it.polimi.ingsw.net;

import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

/**
 * Represents the header of the request or a response.
 */
public class Header implements JSONSerializable {
    private static final long serialVersionUID = -5858895441251305795L;

    /**
     * The user's token.
     */
    @JSONElement("client-token")
    private String clientToken;

    /**
     * The endpoint function.
     */
    @JSONElement("endpoint")
    private EndPointFunction endPointFunction;

    @JSONElement("uuid")
    private final Integer uuid;

    public Header(String clientToken) {
        this(clientToken, null, null);
    }

    public Header(EndPointFunction endPointFunction) {
        this(null, endPointFunction, null);
    }
    
    public Header(EndPointFunction endPointFunction, Integer uuid) {
        this(null, endPointFunction, uuid);
    }
    
    public Header(String clientToken, EndPointFunction endPointFunction) {
        this(clientToken, endPointFunction, null);
    }

    @JSONDesignatedConstructor
    public Header(
            @JSONElement("client-token") String clientToken,
            @JSONElement("endpoint") EndPointFunction endPointFunction,
            @JSONElement("uuid") Integer uuid
    ) {
        this.clientToken = clientToken;
        this.endPointFunction = endPointFunction;
        this.uuid = uuid;
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
    public String toString() {
        return this.serialize().toString();
    }
}
