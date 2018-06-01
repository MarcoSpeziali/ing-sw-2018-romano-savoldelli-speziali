package it.polimi.ingsw.net;

import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

public class ResponseError implements JSONSerializable {

    private static final long serialVersionUID = 3148193003955269886L;

    private int errorCode; // TODO: turn into enum
    private String reason;
    private String localizedReason;

    public ResponseError() {}

    public ResponseError(int errorCode, String reason, String localizedReason) {
        this.errorCode = errorCode;
        this.reason = reason;
        this.localizedReason = localizedReason;
    }

    public ResponseError(int errorCode, String reason) {
        this(errorCode, reason, null);
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        this.errorCode = jsonObject.getInt("code");
        this.reason = jsonObject.getString("reason");
        this.localizedReason = jsonObject.getString("localized-reason");
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("code", this.errorCode);
        jsonObject.put("reason", this.reason);
        jsonObject.put("localized-reason", this.localizedReason);

        return jsonObject;
    }
}
