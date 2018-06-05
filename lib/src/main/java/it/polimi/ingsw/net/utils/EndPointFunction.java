package it.polimi.ingsw.net.utils;

// TODO: docs
public enum EndPointFunction {
    REQUEST_AUTHENTICATION("sign-in/request_authentication"),
    FULFILL_AUTHENTICATION_CHALLENGE("sign-in/fulfill_challenge"),
    SIGN_UP("sign-up"),
    LOOK_UP("look-up");

    private final String endPointFunctionName;

    EndPointFunction(String endPointFunctionName) {
        this.endPointFunctionName = endPointFunctionName;
    }

    public static EndPointFunction fromEndPointFunctionName(String endPointFunctionName) {
        for (EndPointFunction endPointFunction : EndPointFunction.values()) {
            if (endPointFunction.endPointFunctionName.equals(endPointFunctionName)) {
                return endPointFunction;
            }
        }

        throw new IllegalArgumentException("EndPoint: " + endPointFunctionName + " does not exists");
    }

    @Override
    public String toString() {
        return this.endPointFunctionName;
    }
}
