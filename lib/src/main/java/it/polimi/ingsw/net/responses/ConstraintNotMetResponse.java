package it.polimi.ingsw.net.responses;

import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class ConstraintNotMetResponse implements MatchInteraction {

    private static final long serialVersionUID = 6444281671346095927L;

    private final int matchId;

    @JSONDesignatedConstructor
    public ConstraintNotMetResponse(
            @JSONElement("match-id") int matchId
    ) {
        this.matchId = matchId;
    }

    @Override
    @JSONElement("match-id")
    public int getMatchId() {
        return matchId;
    }
}
