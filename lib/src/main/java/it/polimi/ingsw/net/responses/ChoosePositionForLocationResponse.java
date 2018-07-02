package it.polimi.ingsw.net.responses;

import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class ChoosePositionForLocationResponse implements MatchInteraction {
    
    private static final long serialVersionUID = -7013471146672844322L;
    
    private final int matchId;
    private final int location;
    
    @JSONDesignatedConstructor
    public ChoosePositionForLocationResponse(
            @JSONElement("match-id") int matchId,
            @JSONElement("chosen-location") int location
    ) {
        this.matchId = matchId;
        this.location = location;
    }
    
    @Override
    @JSONElement("match-id")
    public int getMatchId() {
        return matchId;
    }
    
    @JSONElement("chosen-location")
    public int getLocation() {
        return location;
    }
}
