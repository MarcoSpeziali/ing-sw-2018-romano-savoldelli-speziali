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
    
        // ------ WINDOW CHOOSE ------
        MATCH_WINDOW_REQUEST("match/window_request"),
        MATCH_WINDOW_RESPONSE("match/window_response"),
    
        // ------ PLAYER TURN ------
        MATCH_PLAYER_TURN_BEGIN_RESPONSE("match/player/turn/begin_response"),
        MATCH_PLAYER_TURN_END_REQUEST("match/player/turn/end_request"),
        MATCH_PLAYER_TURN_END_RESPONSE("match/player/turn/end_response"),
    
        // ------ TOOL CARDS ------
        MATCH_PLAYER_TOOL_CARD_REQUEST("match/player/tool_card_request"),
        MATCH_PLAYER_TOOL_CARD_RESPONSE("match/player/tool_card_response"),
    
        // ------ PLAYER MOVE ------
        MATCH_PLAYER_MOVE_REQUEST("match/player/move_request"),
    
    MATCH_UPDATE_RESPONSE("match/update_response"),
    MATCH_RESULTS_RESPONSE("match/results_response");

    private final String endPointFunctionName;

    EndPointFunction(String endPointFunctionName) {
        this.endPointFunctionName = endPointFunctionName;
    }

    @Override
    public String toString() {
        return this.endPointFunctionName;
    }
}
