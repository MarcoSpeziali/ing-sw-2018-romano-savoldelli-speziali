package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.controllers.proxies.rmi.MatchRMIProxyController;
import it.polimi.ingsw.net.mocks.ILivePlayer;
import it.polimi.ingsw.net.mocks.ILobby;
import it.polimi.ingsw.net.mocks.IPlayer;
import it.polimi.ingsw.net.mocks.MatchMock;
import it.polimi.ingsw.server.Settings;
import it.polimi.ingsw.server.events.EventDispatcher;
import it.polimi.ingsw.server.events.EventType;
import it.polimi.ingsw.server.events.PlayerEventsListener;
import it.polimi.ingsw.server.net.sockets.AuthenticatedClientHandler;
import it.polimi.ingsw.server.sql.DatabaseLobby;
import it.polimi.ingsw.server.sql.DatabaseMatch;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.unsafe;
import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.wrap;

public class MatchManager implements PlayerEventsListener {
    
    private DatabaseMatch databaseMatch;
    private final List<IPlayer> lobbyPlayers;
    
    private boolean migrationClosed = false;

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
    
    @SuppressWarnings({"squid:S1602", "FieldCanBeLocal"})
    // Lamdbas containing only one statement should not nest this statement in a block
    private final FunctionalExceptionWrapper.UnsafeRunnable connectionTimerExecutorTask = () -> {
        // on expiration match starts:
        // the players that did not respond to migration request
        // are marked as left from the lobby, they cannot join back
        migrationClosed = true;
    
        ILobby lobby = this.databaseMatch.getLobby();
        
        List<IPlayer> lobbyPlayers = Arrays.asList(lobby.getPlayers());
        List<IPlayer> matchPlayers = Arrays.stream(this.databaseMatch.getPlayers())
                .map(ILivePlayer::getPlayer)
                .collect(Collectors.toList());
        
        List<IPlayer> timedOutPlayers = new ArrayList<>(lobbyPlayers);
        timedOutPlayers.removeAll(matchPlayers);
    
        try {
            timedOutPlayers.forEach(wrap(iPlayer -> {
                DatabaseLobby.removePlayer(lobby.getId(), iPlayer.getId());
            }));
        }
        catch (FunctionalExceptionWrapper e) {
            e.tryFinalUnwrap(SQLException.class);
        }
    };

    private final Runnable moveTimerExecutorTask = () -> {
        // on expiration turn is over and the next player is awaken
    };
    
    public MatchMock getMatch() {
        // FIXME: correct with more info
        return new MatchMock(this.databaseMatch);
    }
    
    public MatchManager(int lobbyId) throws SQLException {
        this.databaseMatch = DatabaseMatch.insertMatchFromLobby(lobbyId);
        this.lobbyPlayers = Arrays.asList(DatabaseLobby.getLobbyWithId(lobbyId).getPlayers());

        // creates the executor service
        this.matchExecutorService = Executors.newScheduledThreadPool(1);
        
        this.connectionTimerScheduledFuture = matchExecutorService.scheduleWithFixedDelay(
                unsafe(this.connectionTimerExecutorTask),
                Settings.getSettings().getMatchConnectionTimerDuration(),
                Long.MAX_VALUE, // prevents the execution (gives time to cancel the timer)
                Settings.getSettings().getMatchConnectionTimerTimeUnit()
        );
    }
    
    public void addPlayer(DatabasePlayer databasePlayer, AuthenticatedClientHandler authenticatedClientHandler) throws SQLException {
        if (canAcceptPlayer(databasePlayer)) {
            this.socketPlayersHandlers.put(databasePlayer, authenticatedClientHandler);
        }
    }
    
    public void addPlayer(DatabasePlayer databasePlayer, MatchRMIProxyController matchController) throws SQLException {
        if (canAcceptPlayer(databasePlayer)) {
            this.rmiPlayersHandlers.put(databasePlayer, matchController);
    
            if (this.rmiHeartBeatScheduledFuture.isCancelled()) {
                this.setUpHeartBeat();
            }
        }
    }
    
    public void addPlayerCommons(DatabasePlayer databasePlayer) throws SQLException {
        this.databaseMatch = DatabaseMatch.insertPlayer(this.databaseMatch.getId(), databasePlayer.getId());
    
        IPlayer[] players = Arrays.stream(this.databaseMatch.getPlayers())
                .map(ILivePlayer::getPlayer)
                .toArray(IPlayer[]::new);
        
        if (players.length == this.lobbyPlayers.size()) {
            this.connectionTimerScheduledFuture.cancel(true);
        }
    }
    
    private boolean canAcceptPlayer(DatabasePlayer player) throws SQLException {
        return lobbyPlayers.contains(player) && !this.migrationClosed || this.databaseMatch.getLeftPlayers().contains(player);
    }
    
    /**
     * Sets up the heart beat timer.
     */
    private void setUpHeartBeat() {
        this.rmiHeartBeatScheduledFuture = this.matchExecutorService.scheduleAtFixedRate(() -> {
                    List<DatabasePlayer> scheduledForDeletion = new LinkedList<>();
            
                    //noinspection Duplicates
                    this.rmiPlayersHandlers.forEach((player, matchRMIProxyController) -> {
                        try {
                            if (!matchRMIProxyController.onHeartBeat()) {
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
                Settings.getSettings().getRmiHeartBeatMatchTimeout(),
                Settings.getSettings().getRmiHeartBeatMatchTimeUnit()
        );
    }
    
    @Override
    public void onPlayerDisconnected(DatabasePlayer player) {
    
    }
    
    private enum MatchState {
        WAITING_FOR_PLAYERS,
        WAITING_FOR_WINDOW_RESPONSES,
        WAITING_FOR_CONTROLLERS_ACK,
        RUNNING_ROUNDS,
        CALCULATING_RESULTS,
        CLOSING_MATCH
    }
}
