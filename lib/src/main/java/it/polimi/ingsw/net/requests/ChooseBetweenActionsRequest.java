package it.polimi.ingsw.net.requests;

import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.net.mocks.ActionMock;
import it.polimi.ingsw.net.mocks.IAction;
import it.polimi.ingsw.utils.Range;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;
import org.json.JSONObject;

public class ChooseBetweenActionsRequest implements MatchInteraction {

    private static final long serialVersionUID = -2024469814154778765L;

    private final int matchId;
    private final ActionMock[] actions;
    private final Range<Integer> actionsRange;

    public ChooseBetweenActionsRequest(int matchId, ActionMock[] actions, Range<Integer> range) {
        this.matchId = matchId;
        this.actions = actions;
        this.actionsRange = range;
    }

    @JSONDesignatedConstructor
    ChooseBetweenActionsRequest(
            @JSONElement("match-id") int matchId,
            @JSONElement("actions") ActionMock[] actions,
            @JSONElement(value = "actions-range", keepRaw = true) JSONObject jsonObject
            ) {
        this.matchId = matchId;
        this.actions = actions;
        this.actionsRange = Range.fromString(jsonObject.getString("range-str"), "::", Integer::valueOf);
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
