package it.polimi.ingsw.net.responses;

import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class MatchBeginResponse implements MatchInteraction {
    
    private static final long serialVersionUID = -5965057703638665514L;
    
    private final int matchId;
    private final int timeRemaining;

    @JSONDesignatedConstructor
    public MatchBeginResponse(
            @JSONElement("match-id") int matchId,
            @JSONElement("time-remaining") int timeRemaining
    ) {
        this.matchId = matchId;
        this.timeRemaining = timeRemaining;
    }
    
    @Override
    @JSONElement("match-id")
    public int getMatchId() {
        return matchId;
    }
    
    @JSONElement("time-remaining")
    public int getTimeRemaining() {
        return timeRemaining;
    }
}
