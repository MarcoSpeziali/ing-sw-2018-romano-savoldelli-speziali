package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.controllers.proxies.rmi.MatchRMIProxyController;
import it.polimi.ingsw.net.mocks.ILivePlayer;
import it.polimi.ingsw.server.events.PlayerEventsListener;
import it.polimi.ingsw.server.net.sockets.AuthenticatedClientHandler;
import it.polimi.ingsw.server.sql.DatabaseMatch;
import it.polimi.ingsw.server.sql.DatabasePlayer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class MatchManager implements PlayerEventsListener {

    private DatabaseMatch databaseMatch;

    private final ScheduledExecutorService matchExecutorService;

    /**
     * A {@link Map} containing the {@link MatchRMIProxyController} associated to a {@link DatabasePlayer}.
     */
    private final Map<DatabasePlayer, MatchRMIProxyController> rmiPlayersHandlers = new HashMap<>();

    /**
     * A {@link Map} containing the {@link AuthenticatedClientHandler} associated to a {@link DatabasePlayer}.
     */
    private final Map<DatabasePlayer, AuthenticatedClientHandler> socketPlayersHandlers = new HashMap<>();

    private final Map<DatabasePlayer, ILivePlayer> databaseToLivePlayerMap = new HashMap<>();

    /**
     * The {@link ScheduledFuture} that sends the rmi heartbeat.
     */
    private ScheduledFuture<?> rmiHeartBeatScheduledFuture;

    /**
     * The {@link ScheduledFuture} that handles the connection timer.
     */
    private ScheduledFuture<?> connectionTimerScheduledFuture;

    /**
     * The {@link ScheduledFuture} that handles the move timer.
     */
    private ScheduledFuture<?> moveTimerScheduledFuture;

    private final Runnable connectionTimerExecutorTask = () -> {
        // on expiration match starts
    };

    private final Runnable moveTimerExecutorTask = () -> {
        // on expiration turn is over and the next player is awaken
    };

    public MatchManager(int lobbyId) throws SQLException {
        this.databaseMatch = DatabaseMatch.insertMatchFromLobby(lobbyId);

        // creates the executor service
        this.matchExecutorService = Executors.newScheduledThreadPool(1);
    }
}
