package it.polimi.ingsw.server.net.authentication;

import it.polimi.ingsw.net.Body;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.ResponseError;
import it.polimi.ingsw.net.interfaces.SignInInterface;
import it.polimi.ingsw.server.db.DatabasePlayer;
import it.polimi.ingsw.server.db.DatabasePreAuthenticationSession;
import it.polimi.ingsw.server.utils.ServerLogger;
import it.polimi.ingsw.utils.HashUtils;
import it.polimi.ingsw.utils.text.RandomString;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class SignInManager implements SignInInterface {

    private final Request request;
    private final String ip;
    private final int port;

    public SignInManager(Request request, String ip, int port) {
        this.request = request;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public Response requestLogin(String username) {
        String randomString = RandomString.create();

        try {
            DatabasePlayer player = DatabasePlayer.playerWithUsername(username);

            String expectedResult = getExpectedChallengeResult(randomString, player.getPassword());

            DatabasePreAuthenticationSession preAuthenticationSession = DatabasePreAuthenticationSession.insertAuthenticationSession(
                    player.getId(),
                    expectedResult,
                    this.ip,
                    this.port
            );

            return new Response(
                    new Body(
                            this.request.getRequestBody().getEndPointFunction(),
                            Map.of(
                                    "challenge", randomString,
                                    "session-id", preAuthenticationSession.getId()
                            )
                    )
            ); // randomString, sessionId
        }
        catch (SQLException e) {
            ServerLogger.getLogger(SignInManager.class).log(Level.SEVERE, "Error while querying the database", e);

            return new Response(
                    new ResponseError(
                            500,
                            "internal-server-error"
                    )
            ); // server error
        }
        catch (NullPointerException e) {
            return new Response(
                    new Body(
                            this.request.getRequestBody().getEndPointFunction(),
                            Map.of(
                                    "challenge", randomString,
                                    "session-id", -1
                            )
                    )
            ); // user does not exists
        }
    }

    @Override
    public Response fulfillChallenge(int sessionId, String challengeResponse) {
        if (sessionId == -1) {
            return new Response(
                    new ResponseError(
                            401,
                            "unauthorised"
                    )
            ); // error
        }

        try {
            DatabasePreAuthenticationSession preAuthenticationSession = DatabasePreAuthenticationSession.authenticationSessionWithId(sessionId);
            DatabasePlayer player = preAuthenticationSession.getPlayer();

            String expectedResult = preAuthenticationSession.getExpectedChallengeResponse();

            if (expectedResult.equals(challengeResponse)) {
                String rawToken = String.format(
                        "%s%d%d%d",
                        UUID.randomUUID().toString(),
                        sessionId,
                        player.getId(),
                        System.currentTimeMillis()
                );

                String hashedToken = HashUtils.sha1(rawToken);

                if (hashedToken == null) {
                    throw new NoSuchAlgorithmException();
                }

                return new Response(
                        new Body(
                                this.request.getRequestBody().getEndPointFunction(),
                                Map.of("token", hashedToken)
                        )
                ); // token
            }
            else {
                return new Response(
                        new ResponseError(
                                401,
                                "unauthorised"
                        )
                ); // error
            }
        } catch (SQLException e) {
            ServerLogger.getLogger(SignInManager.class).log(Level.SEVERE, "Error while querying the database", e);

            return new Response(
                    new ResponseError(
                            500,
                            "internal-server-error"
                    )
            ); // server error
        } catch (NoSuchAlgorithmException e) {
            ServerLogger.getLogger(SignInManager.class).log(Level.SEVERE, "Could not retrieve algorithm SHA-1", e);

            return new Response(
                    new ResponseError(
                            500,
                            "internal-server-error"
                    )
            ); // server error
        } catch (NullPointerException ignored) {
            return new Response(
                    new ResponseError(
                            401,
                            "unauthorised"
                    )
            ); // error
        }
    }

    private String getExpectedChallengeResult(String randomString, String password) {
        return HashUtils.sha1(randomString + password);
    }
}
