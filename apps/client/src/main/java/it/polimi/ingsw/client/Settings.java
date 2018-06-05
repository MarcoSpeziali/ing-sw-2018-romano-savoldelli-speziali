package it.polimi.ingsw.client;

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

    private final String serverSocketAddress;
    private final int serverSocketPort;

    private final String serverRMIAddress;
    private final int serverRMIPort;

    private final boolean usingSockets;

    public String getServerSocketAddress() {
        return serverSocketAddress;
    }

    public int getServerSocketPort() {
        return serverSocketPort;
    }

    public String getServerRMIAddress() {
        return serverRMIAddress;
    }

    public int getServerRMIPort() {
        return serverRMIPort;
    }

    public boolean isUsingSockets() {
        return usingSockets;
    }

    @SuppressWarnings("squid:UnusedPrivateMethod")
    private Settings(Map<String, String> settings) {
        this.serverSocketPort = Integer.parseInt(settings.get("server-socket-port"));
        this.serverSocketAddress = settings.get("server-socket-address");
        this.serverRMIPort = Integer.parseInt(settings.get("server-rmi-port"));
        this.serverRMIAddress = settings.get("server-rmi-address");
        this.usingSockets = settings.get("connection-protocol").equals("sockets");
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