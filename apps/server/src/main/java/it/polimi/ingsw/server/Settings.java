package it.polimi.ingsw.server;

import it.polimi.ingsw.utils.Setting;
import it.polimi.ingsw.utils.SettingsBase;

import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.unsafe;

// TODO: docs
public final class Settings extends SettingsBase {

    // --------------- GENERIC NETWORK ---------------
    @Setting(id = "socket-port", defaultValue = "9000", type = Integer.class)
    private int socketPort;
    @Setting(id = "rmi-port", defaultValue = "1099", type = Integer.class)
    private int rmiPort;
    @Setting(id = "rmi-host", defaultValue = "idra.weblink.it")
    private String rmiHost;

    // --------------- DATABASE ---------------
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

    // --------------- LOBBY LOGIC ---------------
    @Setting(id = "num-players-timer-start", defaultValue = "2", type = Integer.class)
    private int numberOfPlayersToStartTimer;
    @Setting(id = "lobby-timer-duration", defaultValue = "10000", type = Long.class)
    private long lobbyTimerDuration;
    @Setting(id = "lobby-timer-time-unit", defaultValue = "MILLISECONDS", type = TimeUnit.class)
    private TimeUnit lobbyTimerTimeUnit;
    @Setting(id = "maximum-num-players", defaultValue = "4", type = Integer.class)
    private long maximumNumberOfPlayers;
    @Setting(id = "rmi-heart-beat-lobby", defaultValue = "1000", type = Integer.class)
    private long rmiHeartBeatLobbyTimeout;
    @Setting(id = "rmi-heart-beat-lobby-time-unit", defaultValue = "MILLISECONDS", type = TimeUnit.class)
    private TimeUnit rmiHeartBeatLobbyTimeUnit;
    @Setting(id = "rmi-heart-beat-match", defaultValue = "5", type = Integer.class)
    private long rmiHeartBeatMatchTimeout;
    @Setting(id = "rmi-heart-beat-match-time-unit", defaultValue = "SECONDS", type = TimeUnit.class)
    private TimeUnit rmiHeartBeatMatchTimeUnit;
    
    // --------------- MATCH LOGIC ---------------
    @Setting(id = "match-connection-timer-duration", defaultValue = "30", type = Integer.class)
    private int matchConnectionTimerDuration;
    @Setting(id = "match-connection-timer-time-unit", defaultValue = "SECONDS", type = TimeUnit.class)
    private TimeUnit matchConnectionTimerTimeUnit;
    @Setting(id = "number-of-windows-per-player-to-choose", defaultValue = "4", type = Integer.class)
    private int numberOfWindowsPerPlayerToChoose;
    @Setting(id = "match-move-timer-duration", defaultValue = "90", type = Integer.class)
    private int matchMoveTimerDuration;
    @Setting(id = "match-move-timer-time-unit", defaultValue = "SECONDS", type = TimeUnit.class)
    private TimeUnit matchMoveTimerTimeUnit;
    
        // --------------- MODELS CONSTANTS ---------------
        @Setting(id = "number-of-dice-per-color-in-bag", defaultValue = "18", type = Integer.class)
        private int numberOfDicePerColorInBag;
        @Setting(id = "number-of-private-objective-cards", defaultValue = "1", type = Integer.class)
        private int numberOfPrivateObjectiveCards;
        @Setting(id = "number-of-public-objective-cards", defaultValue = "3", type = Integer.class)
        private int numberOfPublicObjectiveCards;
        @Setting(id = "number-of-tool-cards", defaultValue = "3", type = Integer.class)
        private int numberOfToolCards;
        @Setting(id = "number-of-rounds", defaultValue = "10", type = Byte.class)
        private byte numberOfRounds;
    
    
    private Settings(String path) throws IllegalAccessException {
        super(path);
    }

    public static Settings getSettings() {
        return unsafe(() -> new Settings(Constants.Paths.SETTINGS_PATH.getAbsolutePath()))
                .get();
    }
    
    // --------------- GENERIC NETWORK ---------------
    
    public int getSocketPort() {
        return socketPort;
    }

    public int getRmiPort() {
        return rmiPort;
    }

    public String getRmiHost() {
        return rmiHost;
    }
    
    // --------------- DATABASE ---------------

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
    
    // --------------- LOBBY LOGIC ---------------
    
    public int getNumberOfPlayersToStartTimer() {
        return numberOfPlayersToStartTimer;
    }

    public long getLobbyTimerDuration() {
        return lobbyTimerDuration;
    }
    
    public TimeUnit getLobbyTimerTimeUnit() {
        return lobbyTimerTimeUnit;
    }
    
    public long getMaximumNumberOfPlayers() {
        return maximumNumberOfPlayers;
    }

    public long getRmiHeartBeatLobbyTimeout() {
        return rmiHeartBeatLobbyTimeout;
    }

    public TimeUnit getRmiHeartBeatLobbyTimeUnit() {
        return rmiHeartBeatLobbyTimeUnit;
    }
    
    // --------------- MATCH LOGIC ---------------
    
    public long getRmiHeartBeatMatchTimeout() {
        return rmiHeartBeatMatchTimeout;
    }
    
    public TimeUnit getRmiHeartBeatMatchTimeUnit() {
        return rmiHeartBeatMatchTimeUnit;
    }
    
    public int getMatchConnectionTimerDuration() {
        return matchConnectionTimerDuration;
    }
    
    public TimeUnit getMatchConnectionTimerTimeUnit() {
        return matchConnectionTimerTimeUnit;
    }

    public int getNumberOfWindowsPerPlayerToChoose() {
        return numberOfWindowsPerPlayerToChoose;
    }
    
    public int getMatchMoveTimerDuration() {
        return matchMoveTimerDuration;
    }
    
    public TimeUnit getMatchMoveTimerTimeUnit() {
        return matchMoveTimerTimeUnit;
    }
    
    public byte getNumberOfRounds() {
        return numberOfRounds;
    }
    
        // --------------- MODELS CONSTANTS ---------------
        
        public int getNumberOfDicePerColorInBag() {
            return numberOfDicePerColorInBag;
        }
    
        public int getNumberOfPrivateObjectiveCards() {
            return numberOfPrivateObjectiveCards;
        }
    
        public int getNumberOfPublicObjectiveCards() {
            return numberOfPublicObjectiveCards;
        }
    
        public int getNumberOfToolCards() {
            return numberOfToolCards;
        }
}
