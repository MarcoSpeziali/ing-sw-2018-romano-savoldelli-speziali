package it.polimi.ingsw.net.responses;

import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.net.mocks.IResult;
import it.polimi.ingsw.net.mocks.ResultMock;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class ResultsResponse implements MatchInteraction {

    private static final long serialVersionUID = -5794103174130428362L;

    private final int matchId;
    private final ResultMock[] results;

    @JSONDesignatedConstructor
    public ResultsResponse(
            @JSONElement("match-id") int matchId,
            @JSONElement("results") ResultMock[] results) {
        this.matchId = matchId;
        this.results = results;
    }

    @Override
    @JSONElement("match-id")
    public int getMatchId() {
        return matchId;
    }

    @JSONElement("results")
    public IResult[] getResults() {
        return results;
    }
}
