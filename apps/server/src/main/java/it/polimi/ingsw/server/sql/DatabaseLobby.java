package it.polimi.ingsw.server.sql;

import it.polimi.ingsw.net.mocks.ILobby;
import it.polimi.ingsw.net.mocks.IPlayer;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// TODO: docs
public class DatabaseLobby implements ILobby {

    private static final long serialVersionUID = -6877327242164411945L;

    /**
     * The lobby's unique id.
     */
    private int id;

    /**
     * The lobby's opening time.
     */
    private long openingTime;

    /**
     * The lobby's closing time.
     */
    private long closingTime;

    DatabaseLobby() {
    }

    DatabaseLobby(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.openingTime = resultSet.getDate("opening_time").getTime();
        this.closingTime = resultSet.getDate("closing_time").getTime();
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
            database.executeQuery(query, null);
        }
    }

    public static void removePlayer(int lobbyId, int playerId) throws SQLException {
        String query = String.format(
                "DELETE FROM lobby_player WHERE lobby = '%d' AND player = '%d'",
                lobbyId,
                playerId
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            database.executeQuery(query, null);
        }
    }

    public static DatabaseLobby updateLobby(int id, Map<String, String> updateMap) throws SQLException {
        String update = updateMap.entrySet().stream()
                .map(stringStringEntry -> String.format(
                        "%s = %s",
                        stringStringEntry.getKey(),
                        stringStringEntry.getValue())
                ).reduce("", (s, s2) -> s + ", " + s2);

        String query = String.format(
                "UPDATE lobby SET %s WHERE id = '%d' RETURNING *",
                update,
                id
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            return database.executeQuery(query, DatabaseLobby::new);
        }
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
    public List<IPlayer> getPlayers() {
        String query = String.format(
                "SELECT p.* FROM lobby l " +
                        "JOIN lobby_player lb ON lb.lobby = l.id " +
                        "JOIN player p ON lb.player = p.id " +
                        "WHERE id = '%d'",
                this.id
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            return Collections.unmodifiableList(database.executeQuery(query, resultSet -> {
                LinkedList<IPlayer> databasePlayers = new LinkedList<>();

                do {
                    databasePlayers.add(new DatabasePlayer(resultSet));
                } while (resultSet.next());

                return databasePlayers;
            }));
        }
        catch (SQLException e) {
            return List.of();
        }
    }

    /**
     * Deserialized a {@link JSONObject} into the implementing class.
     *
     * @param jsonObject the {@link JSONObject} to deserialize
     */
    @Override
    public void deserialize(JSONObject jsonObject) {
        throw new UnsupportedOperationException("A database object cannot be deserialized for security reasons");
    }
}