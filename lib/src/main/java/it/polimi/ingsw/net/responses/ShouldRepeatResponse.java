package it.polimi.ingsw.net.responses;

import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class ShouldRepeatResponse implements MatchInteraction {

    private static final long serialVersionUID = -7134371170359783283L;

    private final int matchId;
    private final boolean shouldRepeat;

    @JSONDesignatedConstructor
    public ShouldRepeatResponse(
            @JSONElement("match-id") int matchId,
            @JSONElement("should-repeat") boolean shouldRepeat
    ) {
        this.matchId = matchId;
        this.shouldRepeat = shouldRepeat;
    }

    @Override
    @JSONElement("match-id")
    public int getMatchId() {
        return matchId;
    }

    @JSONElement("should-repeat")
    public boolean shouldRepeat() {
        return shouldRepeat;
    }
}
