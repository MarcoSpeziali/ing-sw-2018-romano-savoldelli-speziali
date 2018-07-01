package it.polimi.ingsw.net.responses;

import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.net.mocks.IPlayer;
import it.polimi.ingsw.net.mocks.IResult;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

import java.util.Map;

public class ResultsResponse implements MatchInteraction {

    private static final long serialVersionUID = -5794103174130428362L;

    private final int matchId;
    private final Map<IPlayer, IResult> resultsMap;

    @JSONDesignatedConstructor
    public ResultsResponse(
            @JSONElement("match-id") int matchId,
            @JSONElement("results-map") Map<IPlayer, IResult> resultsMap) {
        this.matchId = matchId;
        this.resultsMap = resultsMap;
    }

    @Override
    @JSONElement("match-id")
    public int getMatchId() {
        return matchId;
    }

    @JSONElement("results-map")
    public Map<IPlayer, IResult> getResultsMap() {
        return resultsMap;
    }
}
