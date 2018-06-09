package it.polimi.ingsw.net.utils;

// TODO: docs
public enum EndPointFunction {
    SIGN_IN_REQUEST_AUTHENTICATION("sign-in/request_authentication"),
    SIGN_IN_FULFILL_CHALLENGE("sign-in/fulfill_challenge"),
    SIGN_UP("sign-up"),
    LOBBY_JOIN_REQUEST("lobby/join_request"),
    LOBBY_REGISTER_FOR_UPDATES("lobby/register_for_updates");

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
