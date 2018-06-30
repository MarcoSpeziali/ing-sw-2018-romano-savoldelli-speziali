package it.polimi.ingsw.net.requests;

import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class MatchEndRequest implements MatchInteraction {
    
    private static final long serialVersionUID = 6540831775423975180L;
    
    private final int matchId;
    
    @JSONDesignatedConstructor
    public MatchEndRequest(
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
