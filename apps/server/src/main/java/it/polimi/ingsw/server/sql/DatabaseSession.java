package it.polimi.ingsw.server.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

// TODO: docs
public class DatabaseSession {
    private int id;
    private long creationTimeStamp;
    private long invalidationTimeStamp;
    private String token;
    private int preAuthenticationSessionId;

    DatabaseSession(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.creationTimeStamp = resultSet.getTimestamp("creation_time").getTime();
        this.invalidationTimeStamp = resultSet.getTimestamp("invalidation_time").getTime();
        this.token = resultSet.getString("token");
        this.preAuthenticationSessionId = resultSet.getInt("pre_auth_session");
    }

    public static DatabaseSession sessionWithId(int id) throws SQLException {
        String query = String.format(
                "SELECT * FROM session WHERE id = '%d'",
                id
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            return executeQuery(database, query);
        }
    }

    public static DatabaseSession latestActiveSessionForPlayer(DatabasePlayer databasePlayer) throws SQLException {
        String query = String.format(
                "SELECT s.* FROM session s JOIN pre_authentication_session pas ON pas.id = s.pre_auth_session WHERE pas.player = '%d' AND s.invalidation_time > current_timestamp",
                databasePlayer.getId()
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            return executeQuery(database, query);
        }
    }

    public static DatabaseSession sessionWithToken(String token) throws SQLException {
        String query = String.format(
                "SELECT * FROM session WHERE token = '%s'",
                token
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            return executeQuery(database, query);
        }
    }

    public static DatabaseSession insertSession(String token, int preAuthenticationSessionId) throws SQLException {
        String query = String.format(
                "INSERT INTO session (token, pre_auth_session) VALUES ('%s', '%d') RETURNING *",
                token,
                preAuthenticationSessionId
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            return executeQuery(database, query);
        }
    }

    public static DatabaseSession updateSession(int id, Map<String, String> updateMap) throws SQLException {
        String update = updateMap.entrySet().stream()
                .map(stringStringEntry -> String.format(
                        "%s = %s",
                        stringStringEntry.getKey(),
                        stringStringEntry.getValue())
                ).reduce("", (s, s2) -> s.isEmpty() ? s2 : (s + ", " + s2));

        String query = String.format(
                "UPDATE session SET %s WHERE id = '%d' RETURNING *",
                update,
                id
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            return executeQuery(database, query);
        }
    }

    private static DatabaseSession executeQuery(SagradaDatabase database, String query) throws SQLException {
        return database.executeQuery(query, DatabaseSession::new);
    }

    public int getId() {
        return id;
    }

    public long getCreationTimeStamp() {
        return creationTimeStamp;
    }

    public long getInvalidationTimeStamp() {
        return invalidationTimeStamp;
    }

    public String getToken() {
        return token;
    }

    public int getPreAuthenticationSessionId() {
        return preAuthenticationSessionId;
    }

    public DatabasePreAuthenticationSession getPreAuthenticationSession() throws SQLException {
        String query = String.format(
                "SELECT pas.* FROM session s JOIN pre_authentication_session pas ON s.pre_auth_session = pas.id WHERE s.id = '%d'",
                this.id
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            return database.executeQuery(query, DatabasePreAuthenticationSession::new);
        }
    }
}
