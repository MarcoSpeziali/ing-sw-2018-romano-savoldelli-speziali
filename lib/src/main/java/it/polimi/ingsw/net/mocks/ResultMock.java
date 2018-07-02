package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class ResultMock implements IResult {

    private static final long serialVersionUID = -7189132562359713097L;

    private final PlayerMock player;
    private final int match;
    private final int points;

    public ResultMock(IResult iResult) {
        this(
                iResult.getMatchId(),
                new PlayerMock(iResult.getPlayer()),
                iResult.getPoints()
        );
    }

    @JSONDesignatedConstructor
    public ResultMock(
            @JSONElement("match-id") int matchId,
            @JSONElement("player") PlayerMock player,
            @JSONElement("points") int points
    ) {
        this.player = player;
        this.match = matchId;
        this.points = points;
    }

    @Override
    @JSONElement("match")
    public int getMatchId() {
        return match;
    }

    @Override
    @JSONElement("player")
    public IPlayer getPlayer() {
        return player;
    }

    @Override
    @JSONElement("points")
    public int getPoints() {
        return points;
    }
}
