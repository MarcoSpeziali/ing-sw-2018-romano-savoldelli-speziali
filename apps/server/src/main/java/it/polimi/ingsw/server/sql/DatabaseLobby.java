package it.polimi.ingsw.server.sql;

import it.polimi.ingsw.net.mocks.ILobby;
import it.polimi.ingsw.net.mocks.IPlayer;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// TODO: docs
public class DatabaseLobby implements ILobby {

    private static final long serialVersionUID = -6877327242164411945L;

    /**
     * The lobby's unique id.
     */
    private final int id;

    /**
     * The lobby's opening time.
     */
    private final long openingTime;

    /**
     * The lobby's closing time.
     */
    private final long closingTime;

    /**
     * The time remaining before the match starts.
     */
    private int timeRemaining;

    @JSONDesignatedConstructor
    DatabaseLobby() {
        throw new UnsupportedOperationException("A database object cannot be deserialized for security reasons");
    }

    DatabaseLobby(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.openingTime = resultSet.getDate("opening_time").getTime();

        Date date = resultSet.getDate("closing_time");
        this.closingTime = date == null ? -1L : date.getTime();
        this.timeRemaining = -1;
    }

    /**
     * @return the user's unique id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the lobby's opening time
     */
    @Override
    public long getOpeningTime() {
        return openingTime;
    }

    /**
     * @return the lobby's closing time
     */
    @Override
    public long getClosingTime() {
        return closingTime;
    }

    @Override
    public int getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public static DatabaseLobby getLobbyWithId(int id) throws SQLException {
        String query = String.format(
                "SELECT * FROM lobby WHERE id = '%d'",
                id
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            return database.executeQuery(query, DatabaseLobby::new);
        }
    }

    public static DatabaseLobby getOpenLobby() throws SQLException {
        String query = "SELECT * FROM lobby WHERE closing_time IS NULL";

        try (SagradaDatabase database = new SagradaDatabase()) {
            return database.executeQuery(query, DatabaseLobby::new);
        }
    }

    public static DatabaseLobby insertLobby() throws SQLException {
        String query = "INSERT INTO lobby (opening_time) VALUES (DEFAULT) RETURNING *";

        try (SagradaDatabase database = new SagradaDatabase()) {
            return database.executeQuery(query, DatabaseLobby::new);
        }
    }

    public static DatabaseLobby closeLobby(int id) throws SQLException {
        return updateLobby(id, Map.of("closing_time", "current_timestamp"));
    }

    public static void insertPlayer(int lobbyId, int playerId) throws SQLException {
        String query = String.format(
                "INSERT INTO lobby_player (lobby, player) VALUES ('%d', '%d')",
                lobbyId,
                playerId
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            database.executeVoidQuery(query);
        }
    }

    public static void removePlayer(int lobbyId, int playerId) throws SQLException {
        String query = String.format(
                "UPDATE lobby_player SET leaving_time = current_timestamp WHERE lobby = '%d' AND player = '%d'",
                lobbyId,
                playerId
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            database.executeVoidQuery(query);
        }
    }

    public static DatabaseLobby updateLobby(int id, Map<String, String> updateMap) throws SQLException {
        String update = updateMap.entrySet().stream()
                .map(stringStringEntry -> String.format(
                        "%s = %s",
                        stringStringEntry.getKey(),
                        stringStringEntry.getValue())
                ).reduce("", (s, s2) -> s.isEmpty() ? s2 : (s + ", " + s2));

        String query = String.format(
                "UPDATE lobby SET %s WHERE id = '%d' RETURNING *",
                update,
                id
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            return database.executeQuery(query, DatabaseLobby::new);
        }
    }

    @Override
    public IPlayer[] getPlayers() {
        String query = String.format(
                "SELECT p.* FROM lobby l " +
                        "JOIN lobby_player lb ON lb.lobby = l.id " +
                        "JOIN player p ON lb.player = p.id " +
                        "WHERE l.id = '%d' AND lb.leaving_time IS NULL",
                this.id
        );

        //noinspection Duplicates
        try (SagradaDatabase database = new SagradaDatabase()) {
            List<IPlayer> players = database.executeQuery(query, resultSet -> {
                LinkedList<IPlayer> databasePlayers = new LinkedList<>();

                do {
                    databasePlayers.add(new DatabasePlayer(resultSet));
                } while (resultSet.next());

                return databasePlayers;
            });

            if (players == null || players.isEmpty()) {
                return new IPlayer[0];
            }

            return players.toArray(new IPlayer[0]);
        }
        catch (SQLException e) {
            return null;
        }
    }
}