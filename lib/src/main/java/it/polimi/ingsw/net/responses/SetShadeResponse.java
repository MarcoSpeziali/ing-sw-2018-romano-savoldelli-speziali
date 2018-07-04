package it.polimi.ingsw.net.responses;

import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class SetShadeResponse implements MatchInteraction {

    private static final long serialVersionUID = -554243455160523494L;

    private final int matchId;
    private final int chosenShade;

    @JSONDesignatedConstructor
    public SetShadeResponse(
            @JSONElement("match-id") int matchId,
            @JSONElement("chosen-shade") int chosenShade
    ) {
        this.matchId = matchId;
        this.chosenShade = chosenShade;
    }

    @Override
    @JSONElement("match-id")
    public int getMatchId() {
        return matchId;
    }

    @JSONElement("chosen-shade")
    public int getChosenShade() {
        return chosenShade;
    }
}
