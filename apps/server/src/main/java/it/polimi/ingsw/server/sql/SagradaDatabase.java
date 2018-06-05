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

    public ResultSet executeQuery(String query) throws SQLException {
        /*try (Statement statement = this.connection.createStatement()) {
            return statement.executeQuery(query);
        }*/

        return this.connection.createStatement().executeQuery(query);
    }

    @Override
    public void close() throws SQLException {
        this.connection.close();
    }
}
