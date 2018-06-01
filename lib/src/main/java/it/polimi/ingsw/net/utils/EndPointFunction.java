package it.polimi.ingsw.net.utils;

// TODO: docs
public enum EndPointFunction {
    REQUEST_AUTHENTICATION("login/request_authentication"),
    FULFILL_AUTHENTICATION_CHALLENGE("login/fulfill_challenge");

    private final String endPointFunctionName;

    EndPointFunction(String endPointFunctionName) {
        this.endPointFunctionName = endPointFunctionName;
    }

    public String getEndPointFunctionName() {
        return this.endPointFunctionName;
    }

    public static EndPointFunction fromEndPointFunctionName(String endPointFunctionName) {
        for (EndPointFunction endPointFunction : EndPointFunction.values()) {
            if (endPointFunction.endPointFunctionName.equals(endPointFunctionName)) {
                return endPointFunction;
            }
        }

        throw new IllegalArgumentException("EndPoint: " + endPointFunctionName + " does not exists");
    }
}
