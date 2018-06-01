package it.polimi.ingsw.net;

import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

// TODO: docs
public class Request implements JSONSerializable {
    private static final long serialVersionUID = 5889311626900785609L;

    private RequestHeader requestHeader;

    private Body requestBody;

    public Body getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(Body requestBody) {
        this.requestBody = requestBody;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
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
