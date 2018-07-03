package it.polimi.ingsw.net.responses;

import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.net.mocks.ActionMock;
import it.polimi.ingsw.net.mocks.IAction;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class ChooseBetweenActionsResponse implements MatchInteraction {

    private static final long serialVersionUID = -1344470372104348515L;
    private final int matchId;
    private final ActionMock[] chosenActions;

    @JSONDesignatedConstructor
    public ChooseBetweenActionsResponse(
            @JSONElement("match-id") int matchId,
            @JSONElement("chosen-actions") ActionMock[] chosenActions
    ) {
        this.matchId = matchId;
        this.chosenActions = chosenActions;
    }

    @Override
    @JSONElement("match-id")
    public int getMatchId() {
        return matchId;
    }

    @JSONElement("chosen-actions")
    public IAction[] getChosenActions() {
        return chosenActions;
    }
}
