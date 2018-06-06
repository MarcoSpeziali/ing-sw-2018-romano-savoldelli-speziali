package it.polimi.ingsw.server;

import it.polimi.ingsw.utils.SettingsBase;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Map;

// TODO: docs
public final class Settings extends SettingsBase {

    public static Settings getSettings() {
        return (Settings) customSettings;
    }

    private final int socketPort;
    private final int rmiPort;
    private final String rmiHost;
    private final String databaseUrl;
    private final String databaseName;
    private final String databaseUsername;
    private final String databasePassword;
    private final String databaseDriver;

    public int getSocketPort() {
        return socketPort;
    }

    public int getRmiPort() {
        return rmiPort;
    }

    public String getRmiHost() {
        return rmiHost;
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

    @SuppressWarnings("squid:UnusedPrivateMethod")
    private Settings(Map<String, String> settings) {
        this.socketPort = Integer.parseInt(settings.get("socket-port"));
        this.rmiPort = Integer.parseInt(settings.get("rmi-port"));
        this.rmiHost = settings.get("rmi-host");
        this.databaseUrl = settings.get("database-url");
        this.databaseName = settings.get("database-name");
        this.databaseUsername = settings.get("database-username");
        this.databasePassword = settings.get("database-password");
        this.databaseDriver = settings.get("database-driver");
    }

    public static void build() throws IOException, SAXException, ParserConfigurationException {
        SettingsBase.build(
                Settings::new,
                Constants.Resources.DEFAULT_SETTINGS.getRelativePath(),
                Constants.Paths.SETTINGS_PATH.getAbsolutePath(),
                Settings.class
        );
    }
}
