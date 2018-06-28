package it.polimi.ingsw.net.requests;

import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class MoveRequest implements MatchInteraction {
    
    private static final long serialVersionUID = 3254643563679077165L;
    
    private final int matchId;
    private final int from;
    private final int to;
    
    public MoveRequest(int matchId, Move move) {
        this(matchId, move.getDraftPoolPickPosition(), move.getWindowPutPosition());
    }
    
    @JSONDesignatedConstructor
    public MoveRequest(
            @JSONElement("match-id") int matchId,
            @JSONElement("from") int from,
            @JSONElement("to") int to
    ) {
        this.matchId = matchId;
        this.from = from;
        this.to = to;
    }
    
    @Override
    @JSONElement("match-id")
    public int getMatchId() {
        return matchId;
    }
    
    @JSONElement("from")
    public int getFrom() {
        return from;
    }
    
    @JSONElement("to")
    public int getTo() {
        return to;
    }
}
