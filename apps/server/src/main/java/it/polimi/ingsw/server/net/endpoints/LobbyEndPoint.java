package it.polimi.ingsw.server.net.endpoints;

import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.LobbyInterface;
import it.polimi.ingsw.net.interfaces.updates.LobbyUpdatesInterface;
import it.polimi.ingsw.net.mocks.ILobby;
import it.polimi.ingsw.net.requests.LobbyJoinRequest;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.Constants;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

public class LobbyEndPoint extends UnicastRemoteObject implements LobbyInterface, PlayerEventsListener {

    private static final long serialVersionUID = 4733876915509624154L;

    private static LobbyEndPoint instance;
    private static Map<DatabasePlayer, LobbyUpdatesInterface> lobbyUpdates = new HashMap<>();

    protected LobbyEndPoint() throws RemoteException {
        // register this class to listen to the implemented events
        EventDispatcher.register(this);
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
            // the risks in not synchronizing are:
            //      1) creating more than a lobby at the same time
            //      2) inserting more then 4 players in the same lobby
            synchronized (LockManager.getLockObject(Constants.LockTargets.LOBBY)) {
                DatabaseLobby lobby = DatabaseLobby.getOpenLobby();

                // if no lobby are available at this time a new one is created
                if (lobby == null) {
                    lobby = DatabaseLobby.insertLobby();
                }

                // if there's an opened lobby then the player gets inserted into it
                DatabaseLobby.insertPlayer(lobby.getId(), player.getId());

                // notifies every listener
                final DatabaseLobby finalLobby = lobby;
                EventDispatcher.dispatch(EventType.LOBBY_EVENTS, LobbyEventsListener.class, listener -> listener.onPlayerJoined(
                        finalLobby,
                        player
                ));

                // sends the updates to all rmi players
                lobbyUpdates.forEach(
                        (databasePlayer, lobbyUpdatesInterface) -> {
                            try {
                                lobbyUpdatesInterface.onUpdateReceived(finalLobby);
                            }
                            catch (RemoteException e) {
                                ServerLogger.getLogger(SignInEndPoint.class)
                                        .log(Level.SEVERE, "Error while sending the update to rmi client", e);
                            }
                        }
                );
                AuthenticatedClientHandler.getHandlerForPlayersExcept(player)
                        .forEach(client -> {
                                    try {
                                        client.sendResponse(new Response<>(
                                                new Header(EndPointFunction.LOBBY_UPDATE_RESPONSE),
                                                (ILobby) finalLobby
                                        ));
                                    }
                                    catch (IOException e) {
                                        ServerLogger.getLogger(SignInEndPoint.class)
                                                .log(Level.SEVERE, "Error while sending the update to player: " + client.getPlayer(), e);
                                    }
                                }
                        );

                // finally returns the response
                return ResponseFactory.createLobbyResponse(request, lobby);
            }
        }
        catch (SQLException e) {
            ServerLogger.getLogger(SignInEndPoint.class).log(Level.SEVERE, "Error while querying the database", e);

            return ResponseFactory.createInternalServerError(request);
        }
        catch (TimeoutException e) {
            // the user was authenticated, but the token has expired
            return ResponseFactory.createAuthenticationTimeoutError(request);
        }
    }

    @Override
    public Response<ILobby> joinLobby(Request<LobbyJoinRequest> request, LobbyUpdatesInterface lobbyUpdateInterface) {
        try {
            // gets the player from the token
            DatabasePlayer player = AuthenticationManager.getAuthenticatedPlayer(request);

            // if the player is null then the request did not contain the client-token or the token is invalid
            if (player == null) {
                // so an unauthorised response is created and sent
                return ResponseFactory.createUnauthorisedError(request);
            }

            Response<ILobby> lobbyResponse = joinLobby(request);

            synchronized (LockManager.getLockObject(Constants.LockTargets.LOBBY)) {
                // the update-interface gets added
                lobbyUpdates.put(player, lobbyUpdateInterface);

                return lobbyResponse;
            }
        }
        catch (SQLException e) {
            ServerLogger.getLogger(SignInEndPoint.class).log(Level.SEVERE, "Error while querying the database", e);

            return ResponseFactory.createInternalServerError(request);
        }
        catch (TimeoutException e) {
            // the user was authenticated, but the token has expired
            return ResponseFactory.createAuthenticationTimeoutError(request);
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

                    // and dispatches the relative event
                    EventDispatcher.dispatch(EventType.LOBBY_EVENTS, LobbyEventsListener.class, listener -> listener.onPlayerLeft(
                            lobby,
                            player
                    ));

                    // removes the player from the updates
                    lobbyUpdates.remove(player);
                }

                // sends the updates to all rmi players
                lobbyUpdates.forEach(
                        (databasePlayer, lobbyUpdatesInterface) -> {
                            try {
                                lobbyUpdatesInterface.onUpdateReceived(lobby);
                            }
                            catch (RemoteException e) {
                                ServerLogger.getLogger(SignInEndPoint.class)
                                        .log(Level.SEVERE, "Error while sending the update to rmi client", e);
                            }
                        }
                );
                AuthenticatedClientHandler.getHandlerForPlayersExcept(player)
                        .forEach(client -> {
                                    try {
                                        client.sendResponse(new Response<>(
                                                new Header(EndPointFunction.LOBBY_UPDATE_RESPONSE),
                                                (ILobby) lobby
                                        ));
                                    }
                                    catch (IOException e) {
                                        ServerLogger.getLogger(SignInEndPoint.class)
                                                .log(Level.SEVERE, "Error while sending the update to player: " + client.getPlayer(), e);
                                    }
                                }
                        );
            }
        }
        catch (SQLException e) {
            ServerLogger.getLogger(SignInEndPoint.class).log(Level.SEVERE, "Error while querying the database", e);
        }
    }
}
