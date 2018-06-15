package it.polimi.ingsw.server;

import it.polimi.ingsw.utils.Setting;
import it.polimi.ingsw.utils.SettingsBase;

// TODO: docs
public final class Settings extends SettingsBase {

    public static Settings getSettings() {
        try {
            return new Settings(Constants.Paths.SETTINGS_PATH.getAbsolutePath());
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Setting(id = "socket-port", defaultValue = "9000", type = Integer.class)
    private int socketPort;
    @Setting(id = "rmi-port", defaultValue = "1099", type = Integer.class)
    private int rmiPort;
    @Setting(id = "rmi-host", defaultValue = "idra.weblink.it")
    private String rmiHost;
    @Setting(id = "database-url", defaultValue = "localhost:5432")
    private String databaseUrl;
    @Setting(id = "database-name", defaultValue = "sagrada")
    private String databaseName;
    @Setting(id = "database-username", defaultValue = "sagrada")
    private String databaseUsername;
    @Setting(id = "database-password", defaultValue = "jjn6sjI2F34~cicv=aHB]vjqLVw3-CgSbEgFSq}@QMhuuL)DF)zzE$Y5X&FFHGYs")
    private String databasePassword;
    @Setting(id = "database-driver", defaultValue = "postgresql")
    private String databaseDriver;

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

    private Settings(String path) throws IllegalAccessException {
        super(path);
    }
}
