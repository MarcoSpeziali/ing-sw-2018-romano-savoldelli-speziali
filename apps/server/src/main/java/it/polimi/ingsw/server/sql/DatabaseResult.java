package it.polimi.ingsw.server.sql;

import it.polimi.ingsw.net.mocks.IMatch;
import it.polimi.ingsw.net.mocks.IPlayer;
import it.polimi.ingsw.net.mocks.IResult;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseResult implements IResult {

    private static final long serialVersionUID = -524384362675148035L;

    private int playerId;
    private int matchId;
    private int points;

    DatabaseResult (ResultSet resultSet) throws SQLException {
        this.playerId = resultSet.getInt("player");
        this.matchId = resultSet.getInt("match");
        this.points = resultSet.getInt("points");
    }

    @JSONDesignatedConstructor
    DatabaseResult(
            @JSONElement("player") IPlayer player,
            @JSONElement("match") IMatch match,
            @JSONElement("points") int points
    ) {
        this.playerId = player.getId();
        this.matchId = match.getId();
        this.points = points;
    }

    @Override
    @JSONElement("player")
    public IPlayer getPlayer() {
        try {
            return DatabasePlayer.playerWithId(this.playerId);
        }
        catch (SQLException e) {
            return null;
        }
    }

    @Override
    @JSONElement("match-id")
    public int getMatchId() {
        return this.matchId;
    }

    @Override
    @JSONElement("points")
    public int getPoints() {
        return this.points;
    }

    public static DatabaseResult resultForPlayerInMatch(int playerId, int matchId) throws SQLException {
        String query = String.format(
                "SELECT * FROM result WHERE player = %d AND match = %d",
                playerId,
                matchId
        );

        try (SagradaDatabase sagradaDatabase = new SagradaDatabase()) {
            return sagradaDatabase.executeQuery(query, DatabaseResult::new);
        }
    }

    public static DatabaseResult insertResultForPlayerInMatch(int playerId, int matchId, int points) throws SQLException {
        String query = String.format(
                "INSERT INTO result (player, match, points) VALUES (%d, %d, %d) RETURNING *",
                playerId,
                matchId,
                points
        );

        try (SagradaDatabase sagradaDatabase = new SagradaDatabase()) {
            return sagradaDatabase.executeQuery(query, DatabaseResult::new);
        }
    }
}
