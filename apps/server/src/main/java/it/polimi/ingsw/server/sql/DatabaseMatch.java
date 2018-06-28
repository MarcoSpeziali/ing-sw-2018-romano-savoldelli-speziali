package it.polimi.ingsw.server.sql;

import it.polimi.ingsw.net.mocks.*;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DatabaseMatch implements IMatch {

    private static final long serialVersionUID = 6299805944235610022L;

    /**
     * The match unique id.
     */
    private int id;

    /**
     * The match ending time.
     */
    private long startingTime;

    /**
     * The match starting time.
     */
    private long endingTime;

    @JSONDesignatedConstructor
    DatabaseMatch() {
        throw new UnsupportedOperationException("A database object cannot be deserialized for security reasons");
    }

    DatabaseMatch(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.startingTime = resultSet.getDate("starting_time").getTime();

        Date endingDate = resultSet.getDate("ending_time");
        this.endingTime = endingDate == null ? -1L : endingDate.getTime();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public long getStartingTime() {
        return startingTime;
    }

    @Override
    public long getEndingTime() {
        return endingTime;
    }

    @Override
    public ILobby getLobby() {
        String query = String.format(
                "SELECT l.* FROM match m JOIN lobby l ON m.from_lobby = l.id WHERE m.id = %d",
                this.id
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            return database.executeQuery(query, DatabaseLobby::new);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ILivePlayer[] getPlayers() {
        return Arrays.stream(this.getDatabasePlayers())
                .map(LivePlayerMock::new)
                .toArray(ILivePlayer[]::new);
    }
    
    public DatabasePlayer[] getDatabasePlayers() {
        String query = String.format(
                "SELECT p.* FROM match m " +
                        "JOIN match_player mb ON mb.match = m.id " +
                        "JOIN player p ON mb.player = p.id " +
                        "WHERE m.id = '%d' AND mb.leaving_time IS NULL",
                this.id
        );
    
        try (SagradaDatabase database = new SagradaDatabase()) {
            List<DatabasePlayer> players = database.executeQuery(query, resultSet -> {
                LinkedList<DatabasePlayer> databasePlayers = new LinkedList<>();
            
                do {
                    databasePlayers.add(new DatabasePlayer(resultSet));
                } while (resultSet.next());
            
                return databasePlayers;
            });
        
            if (players == null || players.isEmpty()) {
                return new DatabasePlayer[0];
            }
        
            return players.toArray(new DatabasePlayer[0]);
        }
        catch (SQLException e) {
            return null;
        }
    }
    
    public List<IPlayer> getLeftPlayers() throws SQLException {
        String query = String.format(
                "SELECT p.* FROM match m " +
                        "JOIN match_player mb ON mb.match = m.id " +
                        "JOIN player p ON mb.player = p.id " +
                        "WHERE m.id = %d",
                this.id
        );
    
        try (SagradaDatabase database = new SagradaDatabase()) {
            //noinspection Duplicates
            List<IPlayer> result = database.executeQuery(query, resultSet -> {
                LinkedList<IPlayer> databasePlayers = new LinkedList<>();
            
                do {
                    databasePlayers.add(new DatabasePlayer(resultSet));
                } while (resultSet.next());
            
                return databasePlayers;
            });

            return result == null ? List.of() : result;
        }
        catch (SQLException e) {
            return null;
        }
    }

    public static DatabaseMatch matchWithId(int id) throws SQLException {
        String query = String.format(
                "SELECT * FROM match WHERE id = '%d'",
                id
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            return database.executeQuery(query, DatabaseMatch::new);
        }
    }
    
    public static void insertPlayer(int matchId, int playerId) throws SQLException {
        String query = String.format(
                "INSERT INTO match_player (match, player) VALUES (%d, %d)",
                matchId,
                playerId
        );
    
        try (SagradaDatabase database = new SagradaDatabase()) {
            database.executeVoidQuery(query);
        }
    }
    
    public static boolean hasPlayer(int matchId, int playerId) throws SQLException {
        String query = String.format(
                "SELECT EXISTS(SELECT 1 FROM match_player WHERE match = %d AND player = %d)",
                matchId,
                playerId
        );
    
        try (SagradaDatabase database = new SagradaDatabase()) {
            return database.executeQuery(query, Objects::nonNull);
        }
    }

    public static DatabaseMatch getOpenMatches() throws SQLException {
        String query = "SELECT * FROM match WHERE closing_time IS NULL";

        try (SagradaDatabase database = new SagradaDatabase()) {
            return database.executeQuery(query, DatabaseMatch::new);
        }
    }

    public static DatabaseMatch insertMatchFromLobby(int lobbyId) throws SQLException {
        String query = String.format("INSERT INTO match (from_lobby) VALUES (%d) RETURNING *", lobbyId);

        try (SagradaDatabase database = new SagradaDatabase()) {
            return database.executeQuery(query, DatabaseMatch::new);
        }
    }

    public static DatabaseMatch endMatch(int matchId) throws SQLException {
        return updateMatch(matchId, Map.of("ending_time", "current_timestamp"));
    }

    public static DatabaseMatch updateMatch(int id, Map<String, String> updateMap) throws SQLException {
        String update = updateMap.entrySet().stream()
                .map(stringStringEntry -> String.format(
                        "%s = %s",
                        stringStringEntry.getKey(),
                        stringStringEntry.getValue())
                ).reduce("", (s, s2) -> s.isEmpty() ? s2 : (s + ", " + s2));

        String query = String.format(
                "UPDATE match SET %s WHERE id = '%d' RETURNING *",
                update,
                id
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            return database.executeQuery(query, DatabaseMatch::new);
        }
    }

    public static void removePlayer(int matchId, int playerId) throws SQLException {
        String query = String.format(
                "UPDATE match_player SET leaving_time = current_timestamp WHERE match = '%d' AND player = '%d'",
                matchId,
                playerId
        );

        try (SagradaDatabase database = new SagradaDatabase()) {
            database.executeVoidQuery(query);
        }
    }
}
