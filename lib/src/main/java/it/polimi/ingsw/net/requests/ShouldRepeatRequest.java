package it.polimi.ingsw.net.requests;

import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.net.mocks.ActionMock;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class ShouldRepeatRequest implements MatchInteraction {

    private static final long serialVersionUID = 2472134858724495032L;

    private final int matchId;
    private final ActionMock action;

    @JSONDesignatedConstructor
    public ShouldRepeatRequest(
            @JSONElement("match-id") int matchId,
            @JSONElement("action") ActionMock action
    ) {
        this.matchId = matchId;
        this.action = action;
    }

    @Override
    @JSONElement("match-id")
    public int getMatchId() {
        return matchId;
    }

    @JSONElement("action")
    public ActionMock getAction() {
        return action;
    }
}
