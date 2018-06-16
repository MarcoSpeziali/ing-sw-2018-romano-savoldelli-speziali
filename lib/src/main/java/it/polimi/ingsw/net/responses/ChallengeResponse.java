package it.polimi.ingsw.net.responses;

import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

public class ChallengeResponse implements JSONSerializable {

    public static final String CHALLENGE_RESPONSE_FIELD = "challenge-response";
    public static final String SESSION_ID_FIELD = "session-id";
    private static final long serialVersionUID = 2598665017539605644L;
    /**
     * The challenge response to complete as part of the authentication process.
     */
    private String challenge;

    /**
     * The id of the authentication session.
     */
    private int sessionId;

    public ChallengeResponse() {
    }

    public ChallengeResponse(String challenge, int sessionId) {
        this.challenge = challenge;
        this.sessionId = sessionId;
    }

    /**
     * @return the challenge response to complete as part of the authentication process
     */
    public String getChallenge() {
        return challenge;
    }

    /**
     * @param challenge the challenge response to complete as part of the authentication process
     */
    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    /**
     * @return the id of the authentication session
     */
    public int getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId the id of the authentication session
     */
    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        this.challenge = jsonObject.getString(CHALLENGE_RESPONSE_FIELD);
        this.sessionId = jsonObject.getInt(SESSION_ID_FIELD);
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(CHALLENGE_RESPONSE_FIELD, this.challenge);
        jsonObject.put(SESSION_ID_FIELD, this.sessionId);

        return jsonObject;
    }
}
