package it.polimi.ingsw.server.net.endpoints;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.LobbyInterface;
import it.polimi.ingsw.net.interfaces.updates.LobbyUpdatesInterface;
import it.polimi.ingsw.net.mocks.ILobby;
import it.polimi.ingsw.net.requests.LobbyJoinRequest;
import it.polimi.ingsw.server.concurrency.LockTarget;
import it.polimi.ingsw.server.events.EventDispatcher;
import it.polimi.ingsw.server.events.EventType;
import it.polimi.ingsw.server.events.LobbyEventsListener;
import it.polimi.ingsw.server.events.PlayerEventsListener;
import it.polimi.ingsw.server.managers.AuthenticationManager;
import it.polimi.ingsw.server.managers.LockManager;
import it.polimi.ingsw.server.net.ResponseFactory;
import it.polimi.ingsw.server.sql.DatabaseLobby;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.server.utils.ServerLogger;

import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

public class LobbyEndPoint extends UnicastRemoteObject implements LobbyInterface, PlayerEventsListener {

    private static final long serialVersionUID = 4733876915509624154L;

    private static LobbyEndPoint instance;

    public static LobbyEndPoint getInstance() throws RemoteException {
        if (instance == null) {
            instance = new LobbyEndPoint();
        }

        return instance;
    }

    /**
     * The client socket.
     */
    private transient Socket client;

    protected LobbyEndPoint() throws RemoteException {
        // this constructor is needed by UnicastRemoteObject but LobbyEndPoint does't really need it
    }

    @Override
    public Response<ILobby> joinLobby(Request<LobbyJoinRequest> request) throws RemoteException {
        try {
            // since this method needs authentication the user must be authenticated
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
            synchronized (LockManager.getLockObject(LockTarget.LOBBY)) {
                DatabaseLobby lobby = DatabaseLobby.getOpenLobby();

                // if no lobby are available at this time a new one is created
                if (lobby == null) {
                    lobby = DatabaseLobby.insertLobby();
                }

                // if there's an opened lobby then the player gets inserted into it
                DatabaseLobby.insertPlayer(lobby.getId(), player.getId());

                final DatabaseLobby finalLobby = lobby;
                EventDispatcher.dispatch(EventType.LOBBY_EVENTS, LobbyEventsListener.class, listener -> listener.onPlayerJoined(
                        finalLobby,
                        player
                ));

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
    public void registerForRMIUpdate(LobbyUpdatesInterface lobbyUpdateInterface) throws RemoteException {

    }

    @Override
    public void onPlayerDisconnected(DatabasePlayer player) {
        try {
            DatabaseLobby lobby = DatabaseLobby.getOpenLobby();

            if (lobby != null && lobby.getPlayers().contains(player)) {
                DatabaseLobby.removePlayer(lobby.getId(), player.getId());

                EventDispatcher.dispatch(EventType.LOBBY_EVENTS, LobbyEventsListener.class, listener -> listener.onPlayerLeft(
                        lobby,
                        player
                ));
            }
        }
        catch (SQLException e) {
            ServerLogger.getLogger(SignInEndPoint.class).log(Level.SEVERE, "Error while querying the database", e);
        }
    }
}
