package it.polimi.ingsw.server.sql;

import it.polimi.ingsw.server.Settings;

import java.sql.*;

public class SagradaDatabase implements AutoCloseable {

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
        connection.setAutoCommit(true);
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

    @Override
    public void close() throws SQLException {
        this.connection.close();
    }

    @FunctionalInterface
    public interface ResultSetMappingFunction<T> {
        T map(ResultSet resultSet) throws SQLException;
    }
}
