package it.polimi.ingsw.net;

import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

/**
 * Represents the error derived from a request.
 */
public class ResponseError implements JSONSerializable {

    private static final long serialVersionUID = 3148193003955269886L;

    /**
     * The code of the error.
     */
    private int errorCode;

    /**
     * The reason of the error.
     */
    private String reason;

    /**
     * @return the code of the error
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * @return the reason of the error
     */
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
