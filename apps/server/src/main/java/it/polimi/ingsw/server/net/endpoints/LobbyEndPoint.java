package it.polimi.ingsw.server.net.endpoints;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.LobbyInterface;
import it.polimi.ingsw.net.interfaces.updates.LobbyUpdatesInterface;
import it.polimi.ingsw.net.mocks.ILobby;
import it.polimi.ingsw.net.requests.LobbyJoinRequest;
import it.polimi.ingsw.server.managers.AuthenticationManager;
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

public class LobbyEndPoint extends UnicastRemoteObject implements LobbyInterface {

    private static final long serialVersionUID = 4733876915509624154L;

    /**
     * The client socket.
     */
    private transient Socket client;

    public LobbyEndPoint() throws RemoteException { }

    @Override
    public Response<ILobby> joinLobby(Request<LobbyJoinRequest> request) throws RemoteException {
        try {
            DatabasePlayer player = AuthenticationManager.getAuthenticatedPlayer(request);

            if (player == null) {
                return ResponseFactory.createUnauthorisedError(request);
            }

            // TODO: synchronize between clients {

            DatabaseLobby lobby = DatabaseLobby.getOpenLobby();

            if (lobby != null) {
                // TODO: add player to lobby
                DatabaseLobby.insertPlayer(lobby.getId(), player.getId());

                return ResponseFactory.createLobbyResponse(request, lobby);
            }
            else {
                DatabaseLobby newLobby = DatabaseLobby.insertLobby();

                // TODO: add player to lobby
                DatabaseLobby.insertPlayer(newLobby.getId(), player.getId());
            }

            // }
        }
        catch (SQLException e) {
            ServerLogger.getLogger(SignInEndPoint.class).log(Level.SEVERE, "Error while querying the database", e);

            return ResponseFactory.createInternalServerError(request);
        }
        catch (TimeoutException e) {
            return ResponseFactory.createAuthenticationTimeoutError(request);
        }


        return null;
    }

    @Override
    public void registerForRMIUpdate(LobbyUpdatesInterface lobbyUpdateInterface) throws RemoteException {

    }
}
