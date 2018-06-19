package it.polimi.ingsw.net.requests;

import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

public class ChallengeRequest implements JSONSerializable {

    private static final long serialVersionUID = -9158440026090712352L;

    /**
     * The challenge to complete as part of the authentication process.
     */
    @JSONElement("challenge")
    private String challenge;

    /**
     * The id of the authentication session.
     */
    @JSONElement("session-id")
    private int sessionId;

    @JSONDesignatedConstructor
    public ChallengeRequest(
            @JSONElement("challenge") String challenge,
            @JSONElement("session-id") int sessionId
    ) {
        this.challenge = challenge;
        this.sessionId = sessionId;
    }

    /**
     * @return the challenge to complete as part of the authentication process
     */
    public String getChallenge() {
        return challenge;
    }

    /**
     * @param challenge the challenge to complete as part of the authentication process
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
