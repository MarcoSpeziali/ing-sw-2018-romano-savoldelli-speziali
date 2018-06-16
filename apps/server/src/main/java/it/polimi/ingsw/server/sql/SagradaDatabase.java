package it.polimi.ingsw.server.sql;

import it.polimi.ingsw.server.Settings;

import java.sql.*;

public class SagradaDatabase implements AutoCloseable {

    private static boolean autoCommit = true;
    private Connection connection;

    public SagradaDatabase() throws SQLException {
        connection = DriverManager.getConnection(
                String.format(
                        "jdbc:%s://%s/%s",
                        Settings.getSettings().getDatabaseDriver(),
                        Settings.getSettings().getDatabaseUrl(),
                        Settings.getSettings().getDatabaseName()
                ),
                Settings.getSettings().getDatabaseUsername(),
                Settings.getSettings().getDatabasePassword()
        );
        connection.setAutoCommit(autoCommit);
    }

    public static String quote(String original) {
        return String.format("'%s'", original);
    }

    public static void disableAutoCommit(Runnable runnable) {
        autoCommit = false;
        runnable.run();
        autoCommit = true;
    }

    public <T> T executeQuery(String query, ResultSetMappingFunction<T> mapper) throws SQLException {
        try (Statement statement = this.connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(query)) {
                if (mapper != null && resultSet.next()) {
                    return mapper.map(resultSet);
                }

                return null;
            }
        }
    }

    public void executeVoidQuery(String query) throws SQLException {
        try (Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(query);
        }
    }

    @Override
    public void close() throws SQLException {
        this.connection.close();
    }

    @FunctionalInterface
    public interface ResultSetMappingFunction<T> {
        T map(ResultSet resultSet) throws SQLException;
    }
}
