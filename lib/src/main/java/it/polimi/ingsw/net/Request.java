package it.polimi.ingsw.net;

import it.polimi.ingsw.net.utils.RequestFields;
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
    public RequestHeader getHeader() {
        return requestHeader;
    }

    /**
     * Sets the request header.
     * @param requestHeader the request header
     */
    public void setHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    /**
     * @return the request body
     */
    public Body getBody() {
        return requestBody;
    }

    /**
     * Sets the request body.
     * @param requestBody the request body
     */
    public void setBody(Body requestBody) {
        this.requestBody = requestBody;
    }

    public Request() { }

    public Request(RequestHeader requestHeader, Body requestBody) {
        this.requestHeader = requestHeader;
        this.requestBody = requestBody;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        jsonObject = jsonObject.getJSONObject(RequestFields.REQUEST.toString());

        if (jsonObject.has(RequestFields.Header.HEADER.toString())) {
            this.requestHeader = new RequestHeader();
            this.requestHeader.deserialize(jsonObject.getJSONObject(RequestFields.Header.HEADER.toString()));
        }

        if (jsonObject.has(RequestFields.Body.BODY.toString())) {
            this.requestBody = new Body();
            this.requestBody.deserialize(jsonObject.getJSONObject(RequestFields.Body.BODY.toString()));
        }
    }

    @Override
    public JSONObject serialize() {
        JSONObject mainJsonObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(
                RequestFields.Header.HEADER.toString(),
                this.requestHeader == null ? new JSONObject() : this.requestHeader.serialize()
        );
        jsonObject.put(
                RequestFields.Body.BODY.toString(),
                this.requestBody == null ? new JSONObject() : this.requestBody.serialize()
        );

        mainJsonObject.put(RequestFields.REQUEST.toString(), jsonObject);

        return mainJsonObject;
    }

    @Override
    public String toString() {
        return this.serialize().toString();
    }
}
