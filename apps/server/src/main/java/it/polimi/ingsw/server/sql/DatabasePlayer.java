package it.polimi.ingsw.server.sql;

import it.polimi.ingsw.net.mocks.IPlayer;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

// TODO: document
public class DatabasePlayer implements IPlayer {

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

    private static DatabasePlayer executeQuery(SagradaDatabase database, String query) throws SQLException {
        return database.executeQuery(query, DatabasePlayer::new);
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

    /**
     * Serialized the implementing class into a {@link JSONObject}.
     *
     * @return a {@link JSONObject} which represents the serialized object
     */
    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", this.id);
        jsonObject.put("username", this.username);
        
        return jsonObject;
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
