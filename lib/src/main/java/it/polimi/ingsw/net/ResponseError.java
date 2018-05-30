package it.polimi.ingsw.net;

import java.io.Serializable;

public class ResponseError implements Serializable {
    @Override
    public String toString() {
        return "ResponseError{" +
                "localizedReason='" + localizedReason + '\'' +
                '}';
    }

    private static final long serialVersionUID = 3148193003955269886L;
    private String reason;
    private String localizedReason;

    public ResponseError(String reason, String localizedReason) {
        this.reason = reason;
        this.localizedReason = localizedReason;
    }
}
