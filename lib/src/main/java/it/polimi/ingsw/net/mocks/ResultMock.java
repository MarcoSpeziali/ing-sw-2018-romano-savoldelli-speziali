package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class ResultMock implements IResult {

    private static final long serialVersionUID = -7189132562359713097L;

    private final PlayerMock player;
    private final MatchMock match;
    private final int points;

    public ResultMock(IResult iResult) {
        this(
                new PlayerMock(iResult.getPlayer()),
                new MatchMock(iResult.getMatch()),
                iResult.getPoints()
        );
    }

    @JSONDesignatedConstructor
    public ResultMock(
            @JSONElement("player") PlayerMock player,
            @JSONElement("match") MatchMock match,
            @JSONElement("points") int points
    ) {
        this.player = player;
        this.match = match;
        this.points = points;
    }

    @Override
    @JSONElement("player")
    public IPlayer getPlayer() {
        return player;
    }

    @Override
    @JSONElement("match")
    public IMatch getMatch() {
        return match;
    }

    @Override
    @JSONElement("points")
    public int getPoints() {
        return points;
    }
}
