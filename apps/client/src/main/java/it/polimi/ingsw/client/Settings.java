package it.polimi.ingsw.client;

public final class Settings {

    private static final Settings settings = new Settings();

    public static Settings get() {
        return settings;
    }

    private String serverUrl;
    private int serverPort;
    private boolean useSockets;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public boolean isUseSockets() {
        return useSockets;
    }

    public void setUseSockets(boolean useSockets) {
        this.useSockets = useSockets;
    }

    private Settings() {
        this.serverPort = 9000;
        this.serverUrl = "localhost";
        this.useSockets = true;
    }
}
