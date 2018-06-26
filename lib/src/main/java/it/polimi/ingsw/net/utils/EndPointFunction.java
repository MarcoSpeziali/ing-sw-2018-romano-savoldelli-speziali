package it.polimi.ingsw.net.utils;

// TODO: docs
public enum EndPointFunction {
    // ------ SIGN IN ------
    SIGN_IN_REQUEST_AUTHENTICATION("sign-in/request_authentication"),
    SIGN_IN_FULFILL_CHALLENGE("sign-in/fulfill_challenge"),

    // ------ SIGN UP ------
    SIGN_UP("sign-up"),

    // ------ LOBBY ------
    LOBBY_JOIN_REQUEST("lobby/join_request"),
    LOBBY_JOIN_REQUEST_RMI("lobby/join_request_rmi"),
    LOBBY_UPDATE_RESPONSE("lobby/update_response"),

    // ------ MATCH ------

        // ------ MATCH MIGRATION ------
        MATCH_MIGRATION("match/migration"),
        MATCH_MIGRATION_RMI("match/migration_rmi"),

    MATCH_WINDOW_REQUEST("match/window_request"),
    MATCH_WINDOW_RESPONSE("match/window_response"),
    MATCH_UPDATE_RESPONSE("match/update_response");

    private final String endPointFunctionName;

    EndPointFunction(String endPointFunctionName) {
        this.endPointFunctionName = endPointFunctionName;
    }

    @Override
    public String toString() {
        return this.endPointFunctionName;
    }
}
