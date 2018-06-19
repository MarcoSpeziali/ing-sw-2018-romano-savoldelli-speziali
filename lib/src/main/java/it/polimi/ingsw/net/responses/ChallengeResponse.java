package it.polimi.ingsw.net.responses;

import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

public class ChallengeResponse implements JSONSerializable {

    private static final long serialVersionUID = 2598665017539605644L;

    /**
     * The challenge response to complete as part of the authentication process.
     */
    @JSONElement("challenge-response")
    private String challenge;

    /**
     * The id of the authentication session.
     */
    @JSONElement("session-id")
    private int sessionId;

    @JSONDesignatedConstructor
    public ChallengeResponse(
            @JSONElement("challenge-response") String challenge,
            @JSONElement("session-id") int sessionId
    ) {
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
}
