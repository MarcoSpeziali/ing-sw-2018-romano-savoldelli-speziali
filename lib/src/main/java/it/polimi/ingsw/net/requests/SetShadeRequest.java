package it.polimi.ingsw.net.requests;

import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.net.mocks.DieMock;
import it.polimi.ingsw.net.mocks.IDie;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class SetShadeRequest implements MatchInteraction {

    private static final long serialVersionUID = 7943032514517976351L;

    private final int matchId;
    private final IDie die;

    @JSONDesignatedConstructor
    public SetShadeRequest(
            @JSONElement("match-id") int matchId,
            @JSONElement("die") DieMock die
    ) {
        this.matchId = matchId;
        this.die = die;
    }

    @Override
    @JSONElement("match-id")
    public int getMatchId() {
        return matchId;
    }

    @JSONElement("die")
    public IDie getDie() {
        return die;
    }
}
