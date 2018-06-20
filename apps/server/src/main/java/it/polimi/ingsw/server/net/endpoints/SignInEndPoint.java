package it.polimi.ingsw.server.net.endpoints;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.SignInInterface;
import it.polimi.ingsw.net.requests.ChallengeRequest;
import it.polimi.ingsw.net.requests.SignInRequest;
import it.polimi.ingsw.net.responses.ChallengeResponse;
import it.polimi.ingsw.net.responses.SignInResponse;
import it.polimi.ingsw.server.Constants;
import it.polimi.ingsw.server.events.EventDispatcher;
import it.polimi.ingsw.server.events.PlayerEventsListener;
import it.polimi.ingsw.server.managers.LockManager;
import it.polimi.ingsw.server.net.ResponseFactory;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.server.sql.DatabasePreAuthenticationSession;
import it.polimi.ingsw.server.sql.DatabaseSession;
import it.polimi.ingsw.server.utils.ServerLogger;
import it.polimi.ingsw.utils.text.HashUtils;
import it.polimi.ingsw.utils.text.RandomString;

import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class SignInEndPoint extends UnicastRemoteObject implements SignInInterface, PlayerEventsListener {

    private static final long serialVersionUID = 360546960239532589L;

    /**
     * The client socket.
     */
    private transient Socket client;

    public SignInEndPoint() throws RemoteException {
        EventDispatcher.register(this);
    }

    public void setSocket(Socket client) {
        this.client = client;
    }

    private String getIp() throws ServerNotActiveException {
        if (this.client == null) {
            return getClientHost();
        }
        else {
            return this.client.getRemoteSocketAddress().toString().split(":")[0].replace("/", "");
        }
    }

    private int getPort() {
        if (this.client == null) { // rmi
            return -1;
        }
        else { // socket
            return this.client.getPort();
        }
    }

    @Override
    public Response<ChallengeRequest> requestSignIn(Request<SignInRequest> request) {
        synchronized (LockManager.getLockObject(Constants.LockTargets.PLAYER)) {
            // creates a random string which represents the challenge for the client to fulfill
            String randomString = RandomString.create();

            try {
                String username = request.getBody().getUsername();

                // retrieves the player from the database with the provided username
                DatabasePlayer player = DatabasePlayer.playerWithUsername(username);

                String playerPassword;
                int playerId;

                // if the player does not exists: a session is created anyway, but a token will be denied
                if (player == null) {
                    // sends back a fake response
                    return ResponseFactory.createAuthenticationChallengeResponse(
                            request,
                            randomString,
                            -1
                    );
                }
                else {
                    if (DatabaseSession.latestActiveSessionForPlayer(player) != null) {
                        return ResponseFactory.createAlreadyExistsError(request);
                    }

                    playerPassword = player.getPassword();
                    playerId = player.getId();
                }

                // the expected result is: sha1(randomString + playerPassword)
                String expectedResult = getExpectedChallengeResult(randomString, playerPassword);

                if (expectedResult == null) {
                    throw new NoSuchAlgorithmException();
                }

                // creates a pre_authentication_session in the database
                DatabasePreAuthenticationSession preAuthenticationSession = DatabasePreAuthenticationSession.insertAuthenticationSession(
                        playerId,
                        expectedResult,
                        this.getIp(),
                        this.getPort()
                );

                // sends back the response
                return ResponseFactory.createAuthenticationChallengeResponse(
                        request,
                        randomString,
                        preAuthenticationSession.getId()
                );

            }
            catch (SQLException e) {
                ServerLogger.getLogger().log(Level.SEVERE, "Error while querying the database", e);

                // sends back an internal server error
                return ResponseFactory.createInternalServerError(request);
            }
            catch (NoSuchAlgorithmException e) {
                ServerLogger.getLogger().log(Level.SEVERE, "Could not retrieve algorithm SHA-1", e);

                return ResponseFactory.createInternalServerError(request);
            }
            catch (ServerNotActiveException e) {
                ServerLogger.getLogger().log(Level.SEVERE, "Unable to get the ip for the rmi user trying to authenticate", e);

                return ResponseFactory.createInternalServerError(request);
            }
        }
    }

    @Override
    public Response<SignInResponse> fulfillChallenge(Request<ChallengeResponse> request) {
        synchronized (LockManager.getLockObject(Constants.LockTargets.PLAYER)) {
            try {
                String challengeResponse = request.getBody().getChallenge();

                int sessionId = request.getBody().getSessionId();

                if (sessionId == -1) {
                    return ResponseFactory.createUnauthorisedError(request);
                }

                // retrieves the authentication session from the provided id
                DatabasePreAuthenticationSession preAuthenticationSession = DatabasePreAuthenticationSession.authenticationSessionWithId(sessionId);

                // if the authentication session does not exists then the user is unauthorised
                if (preAuthenticationSession == null) {
                    return ResponseFactory.createUnauthorisedError(request);
                }

                // if the authentication session is expired then the user is unauthorised
                if (preAuthenticationSession.getInvalidationTimeStamp() < System.currentTimeMillis()) {
                    return ResponseFactory.createAuthenticationTimeoutError(request);
                }

                // retrieves the player
                DatabasePlayer player = preAuthenticationSession.getPlayer();

                // if, for some reasons, the player does not exists then the user is unauthorised
                if (player == null) {
                    return ResponseFactory.createUnauthorisedError(request);
                }

                // gets the expected response
                String expectedResult = preAuthenticationSession.getExpectedChallengeResponse();

                // if the expected result is the same as the provided one a token can be provided
                // otherwise the user is unauthorised
                if (expectedResult.equals(challengeResponse)) {
                    // the token is generate by concatenating:
                    //  - a uuid
                    //  - the session id
                    //  - the player id
                    //  - the current timestamp
                    String rawToken = String.format(
                            "%s%d%d%d",
                            UUID.randomUUID().toString(),
                            sessionId,
                            player.getId(),
                            System.currentTimeMillis()
                    );

                    // the it is hashed with sha1
                    String hashedToken = HashUtils.sha1(rawToken);

                    // if the hashed token is null an error occurred
                    if (hashedToken == null) {
                        throw new NoSuchAlgorithmException();
                    }

                    // inserts the session
                    DatabaseSession.insertSession(hashedToken, preAuthenticationSession.getId());

                    // the response is finally sent
                    return ResponseFactory.createAuthenticationTokenResponse(request, hashedToken);
                }
                else {
                    return ResponseFactory.createUnauthorisedError(request);
                }
            }
            catch (SQLException e) {
                ServerLogger.getLogger().log(Level.SEVERE, "Error while querying the database", e);

                return ResponseFactory.createInternalServerError(request);
            }
            catch (NoSuchAlgorithmException e) {
                ServerLogger.getLogger().log(Level.SEVERE, "Could not retrieve algorithm SHA-1", e);

                return ResponseFactory.createInternalServerError(request);
            }
        }
    }

    /**
     * @param challenge the challenge
     * @param password  the user password
     * @return the {@code sha1}-hashed version of the concatenation between {@code challenge} and {@code password}
     */
    private String getExpectedChallengeResult(String challenge, String password) {
        return HashUtils.sha1(challenge + password);
    }

    @Override
    public void onPlayerDisconnected(DatabasePlayer player) {
        try {
            synchronized (LockManager.getLockObject(Constants.LockTargets.PLAYER)) {
                DatabaseSession playerSession = DatabaseSession.latestActiveSessionForPlayer(player);

                if (playerSession != null) {
                    DatabaseSession.updateSession(
                            playerSession.getId(),
                            Map.of("invalidation_time", "current_timestamp")
                    );

                    ServerLogger.getLogger().log(
                            Level.INFO,
                            "Invalidated session ({0}) for player ({1}) due to player disconnection",
                            playerSession.getId(),
                            player.getId());
                }
            }
        }
        catch (SQLException e) {
            ServerLogger.getLogger().log(Level.SEVERE, "Error while querying the database", e);
        }
    }
}
