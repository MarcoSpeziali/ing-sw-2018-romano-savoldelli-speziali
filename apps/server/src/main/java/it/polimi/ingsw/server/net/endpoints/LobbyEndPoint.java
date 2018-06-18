package it.polimi.ingsw.server.net.endpoints;

import it.polimi.ingsw.controllers.LobbyController;
import it.polimi.ingsw.controllers.proxies.LobbyRMIProxyController;
import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.LobbyInterface;
import it.polimi.ingsw.net.mocks.ILobby;
import it.polimi.ingsw.net.mocks.IPlayer;
import it.polimi.ingsw.net.mocks.LobbyMock;
import it.polimi.ingsw.net.requests.LobbyJoinRequest;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.Constants;
import it.polimi.ingsw.server.Settings;
import it.polimi.ingsw.server.events.EventDispatcher;
import it.polimi.ingsw.server.events.EventType;
import it.polimi.ingsw.server.events.LobbyEventsListener;
import it.polimi.ingsw.server.events.PlayerEventsListener;
import it.polimi.ingsw.server.managers.AuthenticationManager;
import it.polimi.ingsw.server.managers.LockManager;
import it.polimi.ingsw.server.net.ResponseFactory;
import it.polimi.ingsw.server.net.sockets.AuthenticatedClientHandler;
import it.polimi.ingsw.server.sql.DatabaseLobby;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.server.utils.ServerLogger;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;

public class LobbyEndPoint extends UnicastRemoteObject implements LobbyInterface, PlayerEventsListener {

    private static final long serialVersionUID = 4733876915509624154L;

    private static LobbyEndPoint instance;
    private Map<DatabasePlayer, LobbyRMIProxyController> lobbyControllers = new HashMap<>();

    private final transient ScheduledExecutorService lobbyExecutorService;
    private transient ScheduledFuture<?> rmiHeartBeatScheduledFuture;
    private transient ScheduledFuture<?> timerScheduledFuture;
    private transient int timeRemaining = -1;

    protected LobbyEndPoint() throws RemoteException {
        // register this class to listen to the implemented events
        EventDispatcher.register(this);

        this.lobbyExecutorService = Executors.newScheduledThreadPool(2);
        this.setUpHeartBeat();
    }

    public static LobbyEndPoint getInstance() throws RemoteException {
        if (instance == null) {
            instance = new LobbyEndPoint();
        }

        return instance;
    }

    @Override
    public Response<ILobby> joinLobby(Request<LobbyJoinRequest> request) {
        try {
            // gets the player from the token
            DatabasePlayer player = AuthenticationManager.getAuthenticatedPlayer(request);

            // if the player is null then the request did not contain the client-token or the token is invalid
            if (player == null) {
                // so an unauthorised response is created and sent
                return ResponseFactory.createUnauthorisedError(request);
            }

            // since this method can be accessed by multiple users at the same time
            // the risks in not synchronizing is:
            // inserting more then 4 players in the same lobby
            synchronized (LockManager.getLockObject(Constants.LockTargets.LOBBY)) {
                DatabaseLobby lobby = DatabaseLobby.getOpenLobby();

                // if no lobby are available at this time a new one is created
                if (lobby == null) {
                    lobby = DatabaseLobby.insertLobby();
                }

                // if there's an opened lobby then the player gets inserted into it
                DatabaseLobby.insertPlayer(lobby.getId(), player.getId());

                List<IPlayer> players = lobby.getPlayers();

                // if the number of players in lobby is equal to NumberOfPlayersToStartTimer then the timer is started
                // if the number of players is outside the range [NumberOfPlayersToStartTimer, MaximumNumberOfPlayers]
                // then the timer has already been started
                if (players.size() == Settings.getSettings().getNumberOfPlayersToStartTimer()) {
                    this.timeRemaining = (int) Settings.getSettings().getTimerDurationInMilliseconds();
                    this.timerScheduledFuture = this.lobbyExecutorService.scheduleAtFixedRate(
                            this.executorTask,
                            0,
                            100,
                            TimeUnit.MILLISECONDS
                    );
                }
                // otherwise, if the number of players in lobby is equal to the MaximumNumberOfPlayers then the match stats
                else if (players.size() == Settings.getSettings().getMaximumNumberOfPlayers()) {
                    DatabaseLobby.closeLobby(lobby.getId());
                    this.timerScheduledFuture.cancel(true);
                    this.timeRemaining = -1;
                    // TODO: create match, migrate players

                    final DatabaseLobby finalLobby = lobby;
                    EventDispatcher.dispatch(
                            EventType.LOBBY_EVENTS,
                            LobbyEventsListener.class,
                            listener -> listener.onLobbyClosed(finalLobby, LobbyEventsListener.LobbyCloseReason.MATCH_STARTED)
                    );
                }

                lobby.setTimeRemaining(this.timeRemaining == -1 ? -1 : (this.timeRemaining / 1000));

                // notifies every listener
                final DatabaseLobby finalLobby = lobby;
                EventDispatcher.dispatch(EventType.LOBBY_EVENTS, LobbyEventsListener.class, listener -> listener.onPlayerJoined(
                        finalLobby,
                        player
                ));

                sendUpdates(finalLobby, player);

                // finally returns the response
                return ResponseFactory.createLobbyResponse(request, new LobbyMock(lobby));
            }
        }
        catch (SQLException e) {
            ServerLogger.getLogger().log(Level.SEVERE, "Error while querying the database", e);

            return ResponseFactory.createInternalServerError(request);
        }
        catch (TimeoutException e) {
            // the user was authenticated, but the token has expired
            return ResponseFactory.createAuthenticationTimeoutError(request);
        }
    }

    @Override
    public LobbyController joinLobby(String clientToken) throws RemoteException {
        try {
            // gets the player from the token
            DatabasePlayer player = AuthenticationManager.getAuthenticatedPlayer(clientToken);

            // if the player is null then the request did not contain the client-token or the token is invalid
            if (player == null) {
                // so an unauthorised response is created and sent
                throw new IllegalCallerException();
            }

            Response<ILobby> lobbyResponse = joinLobby(new Request<>(
                    new Header(clientToken, EndPointFunction.LOBBY_JOIN_REQUEST),
                    new LobbyJoinRequest()
            ));

            if (this.rmiHeartBeatScheduledFuture.isCancelled()) {
                this.setUpHeartBeat();
            }

            LobbyRMIProxyController lobbyRMIProxyController = new LobbyRMIProxyController();
            lobbyRMIProxyController.setStartingLobbyValue(lobbyResponse.getBody());

            synchronized (LockManager.getLockObject(Constants.LockTargets.LOBBY)) {
                // the update-interface gets added
                lobbyControllers.put(player, lobbyRMIProxyController);

                return lobbyRMIProxyController;
            }
        }
        catch (SQLException e) {
            ServerLogger.getLogger().log(Level.SEVERE, "Error while querying the database", e);

            throw new RemoteException();
        }
        catch (TimeoutException e) {
            // the user was authenticated, but the token has expired
            throw new RemoteException();
        }
    }

    @Override
    public void onPlayerDisconnected(DatabasePlayer player) {
        try {
            synchronized (LockManager.getLockObject(Constants.LockTargets.LOBBY)) {
                // if the player disconnects it must be removed by the lobby
                DatabaseLobby lobby = DatabaseLobby.getOpenLobby();

                // the lobby could not exist if the player disconnected during a match
                if (lobby != null && lobby.getPlayers().contains(player)) {
                    // removes the player
                    DatabaseLobby.removePlayer(lobby.getId(), player.getId());

                    List<IPlayer> players = lobby.getPlayers();

                    if (players.isEmpty()) {
                        DatabaseLobby.closeLobby(lobby.getId());

                        final DatabaseLobby finalLobby = lobby;
                        EventDispatcher.dispatch(
                                EventType.LOBBY_EVENTS,
                                LobbyEventsListener.class,
                                listener -> listener.onLobbyClosed(finalLobby, LobbyEventsListener.LobbyCloseReason.ALL_PLAYERS_LEFT)
                        );

                        this.lobbyControllers.clear();
                        this.rmiHeartBeatScheduledFuture.cancel(true);

                        return;
                    }

                    // if there are less than NumberOfPlayersToStartTimer then the timer is canceled
                    if (players.size() < Settings.getSettings().getNumberOfPlayersToStartTimer()) {
                        this.timerScheduledFuture.cancel(true);

                        this.timeRemaining = -1;
                        lobby.setTimeRemaining(-1);
                    }

                    lobby.setTimeRemaining(this.timeRemaining / 1000);

                    // and dispatches the relative event
                    final DatabaseLobby finalLobby = lobby;
                    EventDispatcher.dispatch(EventType.LOBBY_EVENTS, LobbyEventsListener.class, listener -> listener.onPlayerLeft(
                            finalLobby,
                            player
                    ));

                    // removes the player from the updates
                    lobbyControllers.remove(player);

                    // send the updates
                    sendUpdates(finalLobby, player);
                }
            }
        }
        catch (SQLException e) {
            ServerLogger.getLogger().log(Level.SEVERE, "Error while querying the database", e);
        }
    }

    private void setUpHeartBeat() {
        this.rmiHeartBeatScheduledFuture = this.lobbyExecutorService.scheduleAtFixedRate(() -> {
                    List<DatabasePlayer> scheduledForDeletion = new LinkedList<>();

                    this.lobbyControllers.forEach((player, lobbyRMIProxyController) -> {
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
                        this.lobbyControllers.remove(player);
                        EventDispatcher.dispatch(EventType.PLAYER_EVENTS, PlayerEventsListener.class, playerEventsListener -> playerEventsListener.onPlayerDisconnected(player));
                    });
                },
                0,
                Settings.getSettings().getRmiHeartBeatTimeout(),
                Settings.getSettings().getRmiHeartBeatTimeUnit()
        );
    }

    private transient Runnable executorTask = () -> {
        if (timeRemaining != -1) {
            timeRemaining -= 100;
        }
    };

    private void sendUpdates(DatabaseLobby databaseLobby, DatabasePlayer exceptTo) {
        lobbyControllers.forEach(
                (databasePlayer, lobbyRMIProxyController) -> {
                    if (!databasePlayer.equals(exceptTo)) {
                        lobbyRMIProxyController.onUpdateReceived(new LobbyMock(databaseLobby));
                    }
                }
        );
        List<AuthenticatedClientHandler> clientsToUpdate = AuthenticatedClientHandler.getHandlerForPlayersExcept(exceptTo);
        clientsToUpdate.forEach(client -> {
                    try {
                        client.sendResponse(new Response<>(
                                new Header(EndPointFunction.LOBBY_UPDATE_RESPONSE),
                                new LobbyMock(databaseLobby)
                        ));
                    }
                    catch (IOException e) {
                        ServerLogger.getLogger()
                                .log(Level.SEVERE, "Error while sending the update to player: " + client.getPlayer(), e);
                    }
                }
        );
    }

    private void sendUpdates(DatabaseLobby databaseLobby) {
        lobbyControllers.forEach(
                (databasePlayer, lobbyRMIProxyController) -> lobbyRMIProxyController.onUpdateReceived(new LobbyMock(databaseLobby))
        );
        databaseLobby.getPlayers().forEach(iPlayer -> {
                    try {
                        AuthenticatedClientHandler.getHandlerForPlayer((DatabasePlayer) iPlayer).sendResponse(new Response<>(
                                new Header(EndPointFunction.LOBBY_UPDATE_RESPONSE),
                                new LobbyMock(databaseLobby)));
                    }
                    catch (IOException e) {
                        ServerLogger.getLogger()
                                .log(Level.SEVERE, "Error while sending the update to player: " + iPlayer, e);
                    }
                }
        );
    }
}
