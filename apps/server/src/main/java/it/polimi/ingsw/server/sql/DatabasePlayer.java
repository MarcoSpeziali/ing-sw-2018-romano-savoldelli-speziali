package it.polimi.ingsw.server.sql;

import it.polimi.ingsw.net.mocks.IPlayer;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

// TODO: document
public class DatabasePlayer implements IPlayer {

    private static final long serialVersionUID = 8654385885910336973L;

    /**
     * The user's unique id.
     */
    private int id;

    /**
     * The user's unique username.
     */
    private String username;

    /**
     * The sha representation of the user's password.
     */
    private String password;

    @JSONDesignatedConstructor
    DatabasePlayer() {
        throw new UnsupportedOperationException("A database object cannot be deserialized for security reasons");
    }

    DatabasePlayer(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.username = resultSet.getString("username");
        this.password = resultSet.getString("password");
    }

    public static DatabasePlayer playerWithId(int id) throws SQLException {
        String query = String.format(
                "SELECT * FROM player WHERE id = '%d'",
                id
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            return executeQuery(database, query);
        }
    }

    public static DatabasePlayer playerWithUsername(String username) throws SQLException {
        String query = String.format(
                "SELECT * FROM player WHERE username = '%s'",
                username
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            return executeQuery(database, query);
        }
    }

    public static DatabasePlayer insertPlayer(String username, String password) throws SQLException {
        String query = String.format(
                "INSERT INTO player (username, password) VALUES ('%s', '%s') RETURNING *",
                username,
                password
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            return executeQuery(database, query);
        }
    }

    public static DatabasePlayer updatePlayer(int id, Map<String, String> updateMap) throws SQLException {
        String update = updateMap.entrySet().stream()
                .map(stringStringEntry -> String.format(
                        "%s = '%s'",
                        stringStringEntry.getKey(),
                        stringStringEntry.getValue())
                ).reduce("", (s, s2) -> s + ", " + s2);

        String query = String.format(
                "UPDATE player SET (%s) WHERE id = '%d' RETURNING *",
                update,
                id
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            return database.executeQuery(query, DatabasePlayer::new);
        }
    }

    public static void deletePlayer(int id) throws SQLException {
        String query = String.format(
                "DELETE FROM player WHERE id = '%d'",
                id
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            database.executeVoidQuery(query);
        }
    }

    private static DatabasePlayer executeQuery(SagradaDatabase database, String query) throws SQLException {
        return database.executeQuery(query, DatabasePlayer::new);
    }

    /**
     * @return the user's unique id
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * @return the user's unique username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * @return the sha representation of the user's password
     */
    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DatabasePlayer player = (DatabasePlayer) o;
        return id == player.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return this.username;
    }
}
