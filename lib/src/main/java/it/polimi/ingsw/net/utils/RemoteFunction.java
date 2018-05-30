package it.polimi.ingsw.net.utils;

public enum RemoteFunction {
    REQUEST_AUTHENTICATION("login/request_authentication"),
    FULFILL_AUTHENTICATION_CHALLENGE("login/fulfill_challenge");

    private final String endPointFunctionName;

    RemoteFunction(String endPointFunctionName) {
        this.endPointFunctionName = endPointFunctionName;
    }

    public String getEndPointFunctionName() {
        return this.endPointFunctionName;
    }
}
