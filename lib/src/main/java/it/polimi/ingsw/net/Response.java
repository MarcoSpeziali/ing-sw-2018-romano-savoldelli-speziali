package it.polimi.ingsw.net;

import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

/**
 * Represents a response to a socket request.
 */
public class Response implements JSONSerializable {

    private static final long serialVersionUID = -3163357263339164222L;

    /**
     * The body of the response.
     */
    private Body body;

    /**
     * The error (if any) of the response.
     */
    private ResponseError responseError;

    /**
     * @return the body of the response
     */
    public Body getBody() {
        return body;
    }

    /**
     * @return the error (if any) of the response
     */
    public ResponseError getResponseError() {
        return responseError;
    }

    public Response() { }

    public Response(Body body, ResponseError error) {
        this.body = body;
        this.responseError = error;
    }

    public Response(Body body) {
        this(body, null);
    }

    public Response(ResponseError error) {
        this(null, error);
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        if (jsonObject.has("body")) {
            this.body = new Body();
            this.body.deserialize(jsonObject.getJSONObject("body"));
        }

        if (jsonObject.has("error")) {
            this.responseError = new ResponseError();
            this.responseError.deserialize(jsonObject.getJSONObject("error"));
        }
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        if (this.body != null) {
            jsonObject.put("body", this.body.serialize());
        }

        if (this.responseError != null) {
            jsonObject.put("error", this.responseError.serialize());
        }

        return jsonObject;
    }

    @Override
    public String toString() {
        return this.serialize().toString();
    }
}
