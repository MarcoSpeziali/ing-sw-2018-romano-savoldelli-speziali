package it.polimi.ingsw.server;

import it.polimi.ingsw.utils.io.XMLUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class Settings {

    private static Settings customSettings;

    public static Settings getSettings() {
        return customSettings;
    }

    private final int socketPort;
    private final String databaseUrl;
    private final String databaseName;
    private final String databaseUsername;
    private final String databasePassword;
    private final String databaseDriver;

    public int getSocketPort() {
        return socketPort;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public String getDatabaseDriver() {
        return databaseDriver;
    }

    private Settings(Map<String, String> settings) {
        this.socketPort = Integer.parseInt(settings.get("socket-port"));
        this.databaseUrl = settings.get("database-url");
        this.databaseName = settings.get("database-name");
        this.databaseUsername = settings.get("database-username");
        this.databasePassword = settings.get("database-password");
        this.databaseDriver = settings.get("database-driver");
    }

    public static void build() throws IOException, SAXException, ParserConfigurationException {
        Node defaultSettingsNode = XMLUtils.parseXmlFromResource("default_settings.xml", Settings.class.getClassLoader());
        Map<String, String> defaultSettingsMap = parseSettingsNode(defaultSettingsNode);

        Settings defaultSettings = new Settings(defaultSettingsMap);

        try {
            Node customSettingsNode = XMLUtils.parseXml(Constants.Paths.SETTINGS_PATH.getAbsolutePath());

            Map<String, String> override = new HashMap<>(defaultSettingsMap);
            override.putAll(parseSettingsNode(customSettingsNode));

            customSettings = new Settings(override);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            customSettings = defaultSettings;
        }
    }

    private static Map<String, String> parseSettingsNode(Node node) {
        NodeList children = node.getChildNodes();

        Map<String, String> settings = new HashMap<>();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);

            if (child.getNodeName().equals("#text")) {
                continue;
            }

            settings.put(child.getNodeName(), child.getTextContent());
        }

        return settings;
    }
}
