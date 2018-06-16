package it.polimi.ingsw.client;

import it.polimi.ingsw.utils.Setting;
import it.polimi.ingsw.utils.SettingsBase;

// TODO: docs
public final class Settings extends SettingsBase {

    private static Settings sharedInstance;
    @Setting(id = "server-socket-address", defaultValue = "idra.weblink.it")
    private String serverSocketAddress;
    @Setting(id = "server-socket-port", defaultValue = "9000", type = Integer.class)
    private int serverSocketPort;
    @Setting(id = "server-rmi-address", defaultValue = "idra.weblink.it")
    private String serverRMIAddress;
    @Setting(id = "server-rmi-port", defaultValue = "1099", type = Integer.class)
    private int serverRMIPort;
    @Setting(id = "connection-protocol", defaultValue = "SOCKETS", type = Constants.Protocols.class)
    private Constants.Protocols protocol;
    @Setting(id = "fullscreen", defaultValue = "true", type = Boolean.class)
    private boolean fullScreenMode;
    @Setting(id = "language", defaultValue = "DEFAULT", type = Constants.Locales.class)
    private Constants.Locales language;

    private Settings(String path) throws IllegalAccessException {
        super(path);
    }

    public static Settings getSettings() {
        try {
            if (sharedInstance == null) {
                sharedInstance = new Settings(Constants.Paths.SETTINGS_PATH.getAbsolutePath());
            }

            return sharedInstance;
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String getServerSocketAddress() {
        return serverSocketAddress;
    }

    public void setServerSocketAddress(String serverSocketAddress) {
        this.serverSocketAddress = serverSocketAddress;
    }

    public int getServerSocketPort() {
        return serverSocketPort;
    }

    public void setServerSocketPort(int serverSocketPort) {
        this.serverSocketPort = serverSocketPort;
    }

    public String getServerRMIAddress() {
        return serverRMIAddress;
    }

    public void setServerRMIAddress(String serverRMIAddress) {
        this.serverRMIAddress = serverRMIAddress;
    }

    public int getServerRMIPort() {
        return serverRMIPort;
    }

    public void setServerRMIPort(int serverRMIPort) {
        this.serverRMIPort = serverRMIPort;
    }

    public Constants.Protocols getProtocol() {
        return protocol;
    }

    public void setProtocol(Constants.Protocols protocol) {
        this.protocol = protocol;
    }

    public boolean isFullScreenMode() {
        return fullScreenMode;
    }

    public void setFullScreenMode(boolean fullScreenMode) {
        this.fullScreenMode = fullScreenMode;
    }

    public Constants.Locales getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = Constants.Locales.valueOf(language);
    }
}