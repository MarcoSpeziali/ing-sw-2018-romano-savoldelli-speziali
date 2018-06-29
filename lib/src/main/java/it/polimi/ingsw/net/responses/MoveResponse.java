package it.polimi.ingsw.net.responses;

import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class MoveResponse implements MatchInteraction {

    private static final long serialVersionUID = -8171128002242013623L;

    private final int matchId;
    private final boolean isValid;

    @JSONDesignatedConstructor
    public MoveResponse(
            @JSONElement("match-id") int matchId,
            @JSONElement("is-valid") boolean isValid) {
        this.matchId = matchId;
        this.isValid = isValid;
    }

    @Override
    @JSONElement("match-id")
    public int getMatchId() {
        return matchId;
    }

    @JSONElement("is-valid")
    public boolean isValid() {
        return isValid;
    }
}
