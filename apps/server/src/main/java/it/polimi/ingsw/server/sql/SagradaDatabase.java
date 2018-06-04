package it.polimi.ingsw.server.sql;

import it.polimi.ingsw.server.Settings;
import it.polimi.ingsw.server.utils.ServerLogger;
import it.polimi.ingsw.utils.io.XMLUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.*;
import java.util.Map;
import java.util.logging.Level;

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
        connection.setAutoCommit(false);
    }

    public ResultSet executeQuery(String query) throws SQLException {
        try (Statement statement = this.connection.createStatement()) {
            return statement.executeQuery(query);
        }
    }

    @Override
    public void close() throws SQLException {
        this.connection.close();
    }
}
