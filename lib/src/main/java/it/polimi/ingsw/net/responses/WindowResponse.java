package it.polimi.ingsw.net.responses;

import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.net.mocks.IWindow;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class WindowResponse implements MatchInteraction {

    private static final long serialVersionUID = -7601470657834051193L;

    private final int matchId;
    private final IWindow chosenWindow;

    @JSONDesignatedConstructor
    public WindowResponse(
            @JSONElement("match-id") int matchId,
            @JSONElement("chosen-window") IWindow chosenWindow
    ) {
        this.matchId = matchId;
        this.chosenWindow = chosenWindow;
    }

    @Override
    @JSONElement("match-id")
    public int getMatchId() {
        return matchId;
    }

    @JSONElement("chosen-window")
    public IWindow getChosenWindow() {
        return chosenWindow;
    }
}
