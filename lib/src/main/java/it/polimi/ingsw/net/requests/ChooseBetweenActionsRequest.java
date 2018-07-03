package it.polimi.ingsw.net.requests;

import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.net.mocks.ActionMock;
import it.polimi.ingsw.net.mocks.IAction;
import it.polimi.ingsw.utils.Range;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class ChooseBetweenActionsRequest implements MatchInteraction {

    private static final long serialVersionUID = -2024469814154778765L;

    private final int matchId;
    private final ActionMock[] actions;
    private final Range<Integer> actionsRange;

    @JSONDesignatedConstructor
    public ChooseBetweenActionsRequest(
            @JSONElement("match-id") int matchId,
            @JSONElement("actions") ActionMock[] actions,
            @JSONElement("actions-range") Range<Integer> actionsRange
    ) {
        this.matchId = matchId;
        this.actions = actions;
        this.actionsRange = actionsRange;
    }

    @Override
    @JSONElement("match-id")
    public int getMatchId() {
        return matchId;
    }

    @JSONElement("actions")
    public IAction[] getActions() {
        return actions;
    }

    @JSONElement("actions-range")
    public Range<Integer> getActionsRange() {
        return actionsRange;
    }
}
