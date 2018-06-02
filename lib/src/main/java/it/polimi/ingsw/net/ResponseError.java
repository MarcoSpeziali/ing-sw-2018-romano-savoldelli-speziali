package it.polimi.ingsw.net;

import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

public class ResponseError implements JSONSerializable {

    private static final long serialVersionUID = 3148193003955269886L;

    private int errorCode;
    private String reason;

    public int getErrorCode() {
        return errorCode;
    }

    public String getReason() {
        return reason;
    }

    public ResponseError() {}

    public ResponseError(int errorCode, String reason) {
        this.errorCode = errorCode;
        this.reason = reason;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        this.errorCode = jsonObject.getInt("code");
        this.reason = jsonObject.getString("reason");
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("code", this.errorCode);
        jsonObject.put("reason", this.reason);

        return jsonObject;
    }
}
