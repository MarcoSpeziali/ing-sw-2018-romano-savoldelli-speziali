package it.polimi.ingsw.net.responses;

import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class ToolCardUsageResponse implements MatchInteraction {

    private static final long serialVersionUID = 8718150799754669467L;

    private final int matchId;

    @JSONDesignatedConstructor
    public ToolCardUsageResponse(
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
