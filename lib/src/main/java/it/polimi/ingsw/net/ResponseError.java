package it.polimi.ingsw.net;

import it.polimi.ingsw.net.utils.ResponseFields;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

/**
 * Represents the error derived from a request.
 */
public class ResponseError implements JSONSerializable {

    private static final long serialVersionUID = 3148193003955269886L;

    /**
     * The code of the error.
     */
    @JSONElement("code")
    private int errorCode;

    /**
     * The reason of the error.
     */
    @JSONElement("reason")
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

    public ResponseError() {
    }

    public ResponseError(ResponseFields.Error error) {
        this(error.getCode(), error.getName());
    }

    @JSONDesignatedConstructor
    public ResponseError(
            @JSONElement("code") int errorCode,
            @JSONElement("reason") String reason
    ) {
        this.errorCode = errorCode;
        this.reason = reason;
    }
}
