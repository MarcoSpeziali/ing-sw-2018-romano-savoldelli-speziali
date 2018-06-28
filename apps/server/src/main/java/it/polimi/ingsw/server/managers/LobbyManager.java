package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.controllers.proxies.rmi.LobbyRMIProxyController;
import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.mocks.IPlayer;
import it.polimi.ingsw.net.mocks.LobbyMock;
import it.polimi.ingsw.net.mocks.MatchMock;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.Constants;
import it.polimi.ingsw.server.Settings;
import it.polimi.ingsw.server.events.*;
import it.polimi.ingsw.server.net.sockets.AuthenticatedClientHandler;
import it.polimi.ingsw.server.sql.DatabaseLobby;
import it.polimi.ingsw.server.sql.DatabaseMatch;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.server.utils.ServerLogger;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;

/**
 * Manages a lobby,
 */
public class LobbyManager implements PlayerEventsListener {
    
    /**
     * The managed {@link DatabaseLobby}.
     */
    private DatabaseLobby databaseLobby;
    
    /**
     * The {@link java.util.concurrent.ExecutorService} uses to start and stop the timer and to sent the rmi heart beat.
     */
    private final ScheduledExecutorService lobbyExecutorService;
    
    /**
     * A {@link Map} containing the {@link LobbyRMIProxyController} associated to a {@link DatabasePlayer}.
     */
    private final Map<DatabasePlayer, LobbyRMIProxyController> rmiPlayersHandlers = new HashMap<>();
    
    /**
     * A {@link Map} containing the {@link AuthenticatedClientHandler} associated to a {@link DatabasePlayer}.
     */
    private final Map<DatabasePlayer, AuthenticatedClientHandler> socketPlayersHandlers = new HashMap<>();
    
    /**
     * The {@link ScheduledFuture} that sends the rmi heartbeat.
     */
    private ScheduledFuture<?> rmiHeartBeatScheduledFuture;
    
    /**
     * The {@link ScheduledFuture} that handles the timer.
     */
    private ScheduledFuture<?> timerScheduledFuture;
    
    /**
     * The time remaining for the timer.
     */
    private int timeRemaining = -1;
    
    /**
     * The {@link Runnable} to execute each timer tick.
     */
    private final Runnable timerExecutorTask = () -> {
        if (timeRemaining == 0) {
            try {
                closeLobbyAndStartMatch();
            }
            catch (SQLException e) {
                ServerLogger.getLogger().log(Level.SEVERE, "Error while querying the database", e);
            }
        }
        else if (timeRemaining != -1) {
            timeRemaining -= 100;
        }
    };
    
    /**
     * Creates a new {@link LobbyManager} and a new {@link DatabaseLobby} which is inserted into the database.
     *
     * @throws SQLException if any sql error occurs
     */
    public LobbyManager() throws SQLException {
        // creates a new lobby in the database
        this.databaseLobby = DatabaseLobby.insertLobby();
        
        // sends the creation event to any listeners
        EventDispatcher.dispatch(
                EventType.LOBBY_EVENTS,
                LobbyEventsListener.class,
                listener -> listener.onLobbyOpened(this.databaseLobby)
        );
        
        // this class registers for PlayerEvents
        EventDispatcher.register(this);
    
        // creates the executor service
        this.lobbyExecutorService = Executors.newScheduledThreadPool(2);
        // sets up the heart beat
        this.setUpHeartBeat();
    }
    
    /**
     * Adds a player into the lobby.
     *
     * @param databasePlayer the player to add
     * @param authenticatedClientHandler the socket handler of the player
     * @throws SQLException if any sql error occurs
     */
    public void addPlayer(DatabasePlayer databasePlayer, AuthenticatedClientHandler authenticatedClientHandler) throws SQLException {
        this.socketPlayersHandlers.put(databasePlayer, authenticatedClientHandler);
        
        this.addPlayerCommons(databasePlayer);
    }
    
    /**
     * Adds a player into the lobby.
     *
     * @param databasePlayer the player to add
     * @param lobbyController the rmi handler of the player
     * @throws SQLException if any sql error occurs
     */
    public void addPlayer(DatabasePlayer databasePlayer, LobbyRMIProxyController lobbyController) throws SQLException {
        this.rmiPlayersHandlers.put(databasePlayer, lobbyController);
        
        if (this.rmiHeartBeatScheduledFuture.isCancelled()) {
            this.setUpHeartBeat();
        }
    
        this.addPlayerCommons(databasePlayer);
    }
    
    /**
     * Handles the commons operation in a player insertion.
     *
     * @param databasePlayer the player to add
     * @throws SQLException if any sql error occurs
     */
    private void addPlayerCommons(DatabasePlayer databasePlayer) throws SQLException {
        DatabaseLobby.insertPlayer(this.databaseLobby.getId(), databasePlayer.getId());
    
        IPlayer[] players = this.databaseLobby.getPlayers();
    
        // if the number of players in lobby is equal to NumberOfPlayersToStartTimer then the timer is started
        // if the number of players is outside the range [NumberOfPlayersToStartTimer, MaximumNumberOfPlayers]
        // then the timer has already been started
        if (players.length == Settings.getSettings().getNumberOfPlayersToStartTimer()) {
            this.timeRemaining = (int) Settings.getSettings().getLobbyTimerDuration();
            this.timerScheduledFuture = this.lobbyExecutorService.scheduleAtFixedRate(
                    this.timerExecutorTask,
                    0,
                    Settings.getSettings().getLobbyTimerDuration() / 100,
                    Settings.getSettings().getLobbyTimerTimeUnit()
            );
        }
        // otherwise, if the number of players in lobby is equal to the MaximumNumberOfPlayers then the match stats
        else if (players.length == Settings.getSettings().getMaximumNumberOfPlayers()) {
            closeLobbyAndStartMatch();
        }
    
        this.databaseLobby.setTimeRemaining(this.timeRemaining == -1 ? -1 : (this.timeRemaining / 1000));
    
        // notifies every listener
        final DatabaseLobby finalLobby = this.databaseLobby;
        EventDispatcher.dispatch(EventType.LOBBY_EVENTS, LobbyEventsListener.class, listener -> listener.onPlayerJoined(
                finalLobby,
                databasePlayer
        ));
    
        sendUpdates(databasePlayer);
    }
    
    /**
     * @return an instance of {@link LobbyMock} created from the current {@link DatabaseLobby}.
     */
    public LobbyMock getDatabaseLobby() {
        return new LobbyMock(this.databaseLobby);
    }
    
    @Override
    public void onPlayerDisconnected(DatabasePlayer player) {
        try {
            synchronized (LockManager.getLockObject(Constants.LockTargets.LOBBY)) {
                // if the player disconnects it must be removed by the lobby
                if (Arrays.stream(this.databaseLobby.getPlayers()).anyMatch(iPlayer -> iPlayer.equals(player))) {
                    // removes the player
                    DatabaseLobby.removePlayer(this.databaseLobby.getId(), player.getId());
                
                    IPlayer[] players = this.databaseLobby.getPlayers();
                
                    if (players.length == 0) {
                        DatabaseLobby.closeLobby(this.databaseLobby.getId());
                    
                        final DatabaseLobby finalLobby = this.databaseLobby;
                        EventDispatcher.dispatch(
                                EventType.LOBBY_EVENTS,
                                LobbyEventsListener.class,
                                listener -> listener.onLobbyClosed(finalLobby, LobbyEventsListener.LobbyCloseReason.ALL_PLAYERS_LEFT)
                        );
                    
                        this.rmiPlayersHandlers.clear();
                        this.rmiHeartBeatScheduledFuture.cancel(true);
                    
                        return;
                    }
                
                    // if there are less than NumberOfPlayersToStartTimer then the timer is canceled
                    if (players.length < Settings.getSettings().getNumberOfPlayersToStartTimer()) {
                        this.timerScheduledFuture.cancel(true);
                    
                        this.timeRemaining = -1;
                        this.databaseLobby.setTimeRemaining(-1);
                    }
    
                    this.databaseLobby.setTimeRemaining(this.timeRemaining / 1000);
                
                    // and dispatches the relative event
                    EventDispatcher.dispatch(EventType.LOBBY_EVENTS, LobbyEventsListener.class, listener -> listener.onPlayerLeft(
                            this.databaseLobby,
                            player
                    ));
                
                    // removes the player from the updates
                    rmiPlayersHandlers.remove(player);
                
                    // send the updates
                    sendUpdates(player);
                }
            }
        }
        catch (SQLException e) {
            ServerLogger.getLogger().log(Level.SEVERE, "Error while querying the database", e);
        }
    }
    
    /**
     * Sets up the heart beat timer.
     */
    private void setUpHeartBeat() {
        this.rmiHeartBeatScheduledFuture = this.lobbyExecutorService.scheduleAtFixedRate(() -> {
                    List<DatabasePlayer> scheduledForDeletion = new LinkedList<>();
            
                    //noinspection Duplicates
                    this.rmiPlayersHandlers.forEach((player, lobbyRMIProxyController) -> {
                        try {
                            if (!lobbyRMIProxyController.onHeartBeat()) {
                                throw new RemoteException();
                            }
                        }
                        catch (RemoteException e) {
                            scheduledForDeletion.add(player);
                        }
                    });
                    
                    scheduledForDeletion.forEach(player -> {
                        this.rmiPlayersHandlers.remove(player);
                        EventDispatcher.dispatch(EventType.PLAYER_EVENTS, PlayerEventsListener.class, playerEventsListener -> playerEventsListener.onPlayerDisconnected(player));
                    });
                },
                0,
                Settings.getSettings().getRmiHeartBeatLobbyTimeout(),
                Settings.getSettings().getRmiHeartBeatLobbyTimeUnit()
        );
    }
    
    /**
     * @throws SQLException if any sql error occurs
     */
    private void closeLobbyAndStartMatch() throws SQLException {
        synchronized (LockManager.getLockObject(Constants.LockTargets.LOBBY)) {
            this.timerScheduledFuture.cancel(false);
            this.timeRemaining = -1;

            MatchManager matchManager = new MatchManager(this.databaseLobby.getId());
            DatabaseLobby.closeLobby(this.databaseLobby.getId());

            EventDispatcher.dispatch(
                    EventType.LOBBY_EVENTS,
                    LobbyEventsListener.class,
                    listener -> listener.onLobbyClosed(this.databaseLobby, LobbyEventsListener.LobbyCloseReason.MATCH_STARTED)
            );
            EventDispatcher.dispatch(
                    EventType.MATCH_EVENTS,
                    MatchEventListeners.class,
                    matchEventListeners -> matchEventListeners.onMatchCreated(matchManager.getDatabaseMatch())
            );
            
            sendMatchMigrationRequest(matchManager.getDatabaseMatch());
        }
    }
    
    private void sendUpdates(DatabasePlayer exceptTo) {
        this.rmiPlayersHandlers.forEach(
                (databasePlayer, lobbyRMIProxyController) -> {
                    if (!databasePlayer.equals(exceptTo)) {
                        lobbyRMIProxyController.onUpdateReceived(new LobbyMock(databaseLobby));
                    }
                }
        );
    
        this.socketPlayersHandlers.forEach((databasePlayer, authenticatedClientHandler) -> {
            if (!databasePlayer.equals(exceptTo)) {
                try {
                    authenticatedClientHandler.sendResponse(new Response<>(
                            new Header(EndPointFunction.LOBBY_UPDATE_RESPONSE),
                            new LobbyMock(databaseLobby)
                    ));
                }
                catch (IOException e) {
                    ServerLogger.getLogger()
                            .log(Level.SEVERE, "Error while sending the update to player: " + databasePlayer, e);
                }
            }
        });
    }
    
    private void sendMatchMigrationRequest(DatabaseMatch databaseMatch) {
        MatchMock matchMock = databaseMatch.toMatchMock();
        
        this.rmiPlayersHandlers.forEach(
                (databasePlayer, lobbyRMIProxyController) -> {
                    ServerLogger.getLogger().log(Level.FINER, () -> String.format(
                            "Sending match (%d) to player (%d) using rmi",
                            matchMock.getId(),
                            databasePlayer.getId()
                    ));
                    
                    lobbyRMIProxyController.postMigrationRequest(matchMock);
                }
        );
    
        this.socketPlayersHandlers.forEach((databasePlayer, authenticatedClientHandler) -> {
                            try {
                                ServerLogger.getLogger().log(Level.FINER, () -> String.format(
                                        "Sending match (%d) to player (%d) using sockets",
                                        matchMock.getId(),
                                        databasePlayer.getId()
                                ));
    
                                authenticatedClientHandler.sendResponse(new Response<>(
                                        new Header(EndPointFunction.MATCH_MIGRATION),
                                        matchMock
                                ));
                            }
                            catch (IOException e) {
                                ServerLogger.getLogger()
                                        .log(Level.SEVERE, "Error while sending the migration request to player: " + databasePlayer, e);
                            }
                        }
                );
    }
}
