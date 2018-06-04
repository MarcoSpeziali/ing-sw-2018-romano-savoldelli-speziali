package it.polimi.ingsw.net;

import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

/**
 * Represents a socket request.
 */
public class Request implements JSONSerializable {
    private static final long serialVersionUID = 5889311626900785609L;

    /**
     * The request header.
     */
    private RequestHeader requestHeader;

    /**
     * The request body.
     */
    private Body requestBody;

    /**
     * @return the request header
     */
    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    /**
     * Sets the request header.
     * @param requestHeader the request header
     */
    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    /**
     * @return the request body
     */
    public Body getRequestBody() {
        return requestBody;
    }

    /**
     * Sets the request body.
     * @param requestBody the request body
     */
    public void setRequestBody(Body requestBody) {
        this.requestBody = requestBody;
    }

    public Request() { }

    public Request(RequestHeader requestHeader, Body requestBody) {
        this.requestHeader = requestHeader;
        this.requestBody = requestBody;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        this.requestHeader = new RequestHeader();
        this.requestHeader.deserialize(jsonObject.getJSONObject("header"));

        this.requestBody = new Body();
        this.requestBody.deserialize(jsonObject.getJSONObject("body"));
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("header", this.requestHeader.serialize());
        jsonObject.put("body", this.requestBody.serialize());

        return jsonObject;
    }
}
