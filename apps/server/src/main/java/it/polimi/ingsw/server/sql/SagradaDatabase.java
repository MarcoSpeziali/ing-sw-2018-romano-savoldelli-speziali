package it.polimi.ingsw.server.sql;

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

    private static Map<String, Object> settings;
    static {
        try {
            settings = XMLUtils.xmlToMap(XMLUtils.parseXmlFromResource("database/settings.xml", SagradaDatabase.class.getClassLoader()));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            ServerLogger.getLogger(SagradaDatabase.class).log(Level.SEVERE, "Could not read database settings", e);
        }
    }

    public SagradaDatabase() throws SQLException {
        connection = DriverManager.getConnection(
                String.format(
                        "jdbc:%s://%s/%s",
                        settings.get("database-jbdc"),
                        settings.get("url"),
                        settings.get("database")
                ),
                (String) settings.get("username"),
                (String) settings.get("password")
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
