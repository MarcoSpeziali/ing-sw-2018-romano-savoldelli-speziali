package it.polimi.ingsw.server.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

// TODO: document
public class DatabasePreAuthenticationSession {
    private int id;
    private int playerId;
    private String expectedChallengeResponse;
    private long creationTimeStamp;
    private long invalidationTimeStamp;
    private String validForIp;
    private int validForPort;

    DatabasePreAuthenticationSession(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.playerId = resultSet.getInt("player");
        this.expectedChallengeResponse = resultSet.getString("expected_challenge_response");
        this.creationTimeStamp = resultSet.getTimestamp("creation_time").getTime();
        this.invalidationTimeStamp = resultSet.getTimestamp("invalidation_time").getTime();
        this.validForIp = resultSet.getString("valid_for_ip");
        this.validForPort = resultSet.getInt("valid_for_port");
    }

    public static DatabasePreAuthenticationSession authenticationSessionWithId(int id) throws SQLException {
        String query = String.format(
                "SELECT * FROM pre_authentication_session WHERE id = '%d'",
                id
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            return executeQuery(database, query);
        }
    }

    public static DatabasePreAuthenticationSession insertAuthenticationSession(int playerId, String expectedChallengeResponse, String validForIp, int validForPort) throws SQLException {
        String query = String.format(
                "INSERT INTO pre_authentication_session (player, expected_challenge_response, valid_for_ip, valid_for_port) VALUES ('%d', '%s', '%s', '%d') RETURNING *",
                playerId,
                expectedChallengeResponse,
                validForIp,
                validForPort
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            return executeQuery(database, query);
        }
    }

    public static DatabasePreAuthenticationSession updateAuthenticationSession(int id, Map<String, String> updateMap) throws SQLException {
        String update = updateMap.entrySet().stream()
                .map(stringStringEntry -> String.format(
                        "%s = %s",
                        stringStringEntry.getKey(),
                        stringStringEntry.getValue())
                ).reduce("", (s, s2) -> s.isEmpty() ? s2 : (s + ", " + s2));

        String query = String.format(
                "UPDATE pre_authentication_session SET %s WHERE id = '%d' RETURNING *",
                update,
                id
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            return executeQuery(database, query);
        }
    }

    private static DatabasePreAuthenticationSession executeQuery(SagradaDatabase database, String query) throws SQLException {
        return database.executeQuery(query, DatabasePreAuthenticationSession::new);
    }

    public int getId() {
        return id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public DatabasePlayer getPlayer() throws SQLException {
        String query = String.format(
                "SELECT p.* FROM pre_authentication_session pas JOIN player p ON pas.player = p.id WHERE pas.id = %d",
                this.id
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            return database.executeQuery(query, DatabasePlayer::new);
        }
    }

    public String getExpectedChallengeResponse() {
        return expectedChallengeResponse;
    }

    public long getCreationTimeStamp() {
        return creationTimeStamp;
    }

    public long getInvalidationTimeStamp() {
        return invalidationTimeStamp;
    }

    public String getValidForIp() {
        return validForIp;
    }

    public int getValidForPort() {
        return validForPort;
    }
}
