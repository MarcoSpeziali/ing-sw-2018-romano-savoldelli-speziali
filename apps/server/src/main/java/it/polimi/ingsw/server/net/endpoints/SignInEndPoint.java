package it.polimi.ingsw.server.net.endpoints;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.SignInInterface;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.server.sql.DatabasePreAuthenticationSession;
import it.polimi.ingsw.server.net.ResponseFactory;
import it.polimi.ingsw.server.utils.ServerLogger;
import it.polimi.ingsw.utils.HashUtils;
import it.polimi.ingsw.utils.text.RandomString;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

public class SignInEndPoint implements SignInInterface {

    /**
     * The client request.
     */
    private Request request;

    /**
     * The client ip.
     */
    private String ip;

    /**
     * The client port.
     */
    private int port;

    public void setRequest(Request request) {
        this.request = request;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public SignInEndPoint(Request request, String ip, int port) {
        this.request = request;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public Response requestLogin(String username) {
        // creates a random string which represents the challenge for the client to fulfill
        String randomString = RandomString.create();

        try {
            // retrieves the player from the database with the provided username
            DatabasePlayer player = DatabasePlayer.playerWithUsername(username);

            String playerPassword;
            int playerId;

            // if the player does not exists: a session is created anyway, but a token will be denied
            if (player == null) {
                playerPassword = null;
                playerId = -1;
            }
            else {
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
                    this.ip,
                    this.port
            );

            // sends back the response
            return ResponseFactory.createAuthenticationChallengeResponse(
                    this.request,
                    randomString,
                    preAuthenticationSession.getId()
            );
        }
        catch (SQLException e) {
            ServerLogger.getLogger(SignInEndPoint.class).log(Level.SEVERE, "Error while querying the database", e);

            // sends back an internal server error
            return ResponseFactory.createInternalServerError();
        } catch (NoSuchAlgorithmException e) {
            ServerLogger.getLogger(SignInEndPoint.class).log(Level.SEVERE, "Could not retrieve algorithm SHA-1", e);

            return ResponseFactory.createInternalServerError();
        }
    }

    @Override
    public Response fulfillChallenge(int sessionId, String challengeResponse) {
        try {
            // retrieves the authentication session from the provided id
            DatabasePreAuthenticationSession preAuthenticationSession = DatabasePreAuthenticationSession.authenticationSessionWithId(sessionId);

            // if the authentication session does not exists then the user is unauthorised
            if (preAuthenticationSession == null) {
                return ResponseFactory.createUnauthorisedError();
            }

            // if the player id is -1 then the session was fake (the player did not exist)
            if (preAuthenticationSession.getPlayerId() == -1) {
                return ResponseFactory.createUnauthorisedError();
            }

            // if the authentication session is expired then the user is unauthorised
            if (preAuthenticationSession.getInvalidationTimeStamp() < System.currentTimeMillis()) {
                return ResponseFactory.createAuthenticationTimeoutError();
            }

            // retrieves the player
            DatabasePlayer player = preAuthenticationSession.getPlayer();

            // if, for some reasons, the player does not exists then the user is unauthorised
            if (player == null) {
                return ResponseFactory.createUnauthorisedError();
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

                // the response is finally sent
                return ResponseFactory.createAuthenticationTokenResponse(this.request, hashedToken);
            }
            else {
                return ResponseFactory.createUnauthorisedError();
            }
        } catch (SQLException e) {
            ServerLogger.getLogger(SignInEndPoint.class).log(Level.SEVERE, "Error while querying the database", e);

            return ResponseFactory.createInternalServerError();
        } catch (NoSuchAlgorithmException e) {
            ServerLogger.getLogger(SignInEndPoint.class).log(Level.SEVERE, "Could not retrieve algorithm SHA-1", e);

            return ResponseFactory.createInternalServerError();
        }
    }

    /**
     * @param challenge the challenge
     * @param password the user password
     * @return the {@code sha1}-hashed version of the concatenation between {@code challenge} and {@code password}
     */
    private String getExpectedChallengeResult(String challenge, String password) {
        return HashUtils.sha1(challenge + password);
    }
}
