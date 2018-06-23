package it.polimi.ingsw.server.net.endpoints;

import it.polimi.ingsw.controllers.LobbyController;
import it.polimi.ingsw.controllers.proxies.rmi.LobbyRMIProxyController;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.LobbyInterface;
import it.polimi.ingsw.net.mocks.ILobby;
import it.polimi.ingsw.net.requests.LobbyJoinRequest;
import it.polimi.ingsw.server.Constants;
import it.polimi.ingsw.server.events.EventDispatcher;
import it.polimi.ingsw.server.events.LobbyEventsListener;
import it.polimi.ingsw.server.utils.AuthenticationHelper;
import it.polimi.ingsw.server.managers.LobbyManager;
import it.polimi.ingsw.server.managers.LockManager;
import it.polimi.ingsw.server.net.ResponseFactory;
import it.polimi.ingsw.server.net.sockets.AuthenticatedClientHandler;
import it.polimi.ingsw.server.sql.DatabaseLobby;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.server.utils.ServerLogger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

public class LobbyEndPoint extends UnicastRemoteObject implements LobbyInterface, LobbyEventsListener {

    private static final long serialVersionUID = 4733876915509624154L;

    private static LobbyEndPoint instance;
    
    private transient LobbyManager lobbyManager;

    protected LobbyEndPoint() throws RemoteException {
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
            DatabasePlayer player = AuthenticationHelper.getAuthenticatedPlayer(request);

            // if the player is null then the request did not contain the client-token or the token is invalid
            if (player == null) {
                // so an unauthorised response is created and sent
                return ResponseFactory.createUnauthorisedError(request);
            }

            // since this method can be accessed by multiple users at the same time
            // the risks in not synchronizing is:
            // inserting more then 4 players in the same lobby
            synchronized (LockManager.getLockObject(Constants.LockTargets.LOBBY)) {
                // if the lobby manager is null then the lobby does not exists
                if (this.lobbyManager == null) {
                    this.lobbyManager = new LobbyManager();
                }
                
                // adds the player to the lobby
                this.lobbyManager.addPlayer(player, AuthenticatedClientHandler.getHandlerForPlayer(player));
                
                // finally returns the response
                return ResponseFactory.createLobbyResponse(request, this.lobbyManager.getDatabaseLobby());
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
            DatabasePlayer player = AuthenticationHelper.getAuthenticatedPlayer(clientToken);

            // if the player is null then the request did not contain the client-token or the token is invalid
            if (player == null) {
                // so an unauthorised response is created and sent
                // FIXME
                throw new IllegalCallerException();
            }
    
            synchronized (LockManager.getLockObject(Constants.LockTargets.LOBBY)) {
                // if the lobby manager is null then the lobby does not exists
                if (this.lobbyManager == null) {
                    this.lobbyManager = new LobbyManager();
                }
    
                LobbyRMIProxyController lobbyRMIProxyController = new LobbyRMIProxyController();

                // adds the player to the lobby
                this.lobbyManager.addPlayer(player, lobbyRMIProxyController);

                lobbyRMIProxyController.setStartingLobbyValue(this.lobbyManager.getDatabaseLobby());

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
    public void onLobbyClosed(DatabaseLobby lobby, LobbyCloseReason reason) {
        this.lobbyManager = null;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
    
        return super.equals(o);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
