package it.polimi.ingsw.client.net.authentication;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.Settings;
import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.SignInInterface;
import it.polimi.ingsw.net.providers.OneTimeNetworkResponseProvider;
import it.polimi.ingsw.net.providers.OneTimeRMIResponseProvider;
import it.polimi.ingsw.net.providers.OneTimeSocketResponseProvider;
import it.polimi.ingsw.net.requests.ChallengeRequest;
import it.polimi.ingsw.net.requests.SignInRequest;
import it.polimi.ingsw.net.responses.ChallengeResponse;
import it.polimi.ingsw.net.responses.SignInResponse;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.net.utils.ResponseFields;
import it.polimi.ingsw.utils.text.HashUtils;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class SignInManager {

    private static SignInManager instance = new SignInManager();
    private String token;
    private OneTimeNetworkResponseProvider oneTimeNetworkResponseProvider;
    private SignInManager() {
        if (Settings.getSettings().getProtocol().equals(Constants.Protocols.SOCKETS)) {
            oneTimeNetworkResponseProvider = new OneTimeSocketResponseProvider(
                    Settings.getSettings().getServerSocketAddress(),
                    Settings.getSettings().getServerSocketPort()
            );
        }
        else {
            oneTimeNetworkResponseProvider = new OneTimeRMIResponseProvider<>(
                    SignInInterface.class,
                    Settings.getSettings().getServerRMIAddress(),
                    Settings.getSettings().getServerRMIPort()
            );
        }
    }

    public static SignInManager getManager() {
        return instance;
    }

    /**
     * Fulfills the challenge.
     *
     * @param challenge the challenge to fulfill
     * @param password  the user password
     * @return the fulfilled challenge
     */
    private static String fulfillChallenge(String challenge, String password) {
        password = HashUtils.sha1(password);

        return HashUtils.sha1(challenge + password);
    }

    /**
     * @return whether the user is currently authenticated
     */
    public boolean isAuthenticated() {
        return this.token != null;
    }

    public String getToken() {
        return token;
    }

    /**
     * Authenticates the user.
     *
     * @param username the username of the player
     * @param password the password of the player
     * @return {@code true} if the user has been successfully authenticated, {@code false} otherwise
     * @throws IOException       if a server error or a connection error occurred
     * @throws NotBoundException if the {@link EndPointFunction} is not bound
     */
    public boolean signIn(String username, String password) throws IOException, NotBoundException {
        // builds the sign-in request
        Request<SignInRequest> authenticationRequest = new Request<>(
                new Header(EndPointFunction.SIGN_IN_REQUEST_AUTHENTICATION),
                new SignInRequest(username)
        );

        // first a login request is made
        Response<ChallengeRequest> requestResponse = this.oneTimeNetworkResponseProvider.getSyncResponseFor(authenticationRequest);

        // the only possible error that can occur is a 500 Internal Server Error
        if (requestResponse.getError() != null) {
            throw new RemoteException();
        }

        int sessionId = requestResponse.getBody().getSessionId();
        String challenge = requestResponse.getBody().getChallenge();

        // builds the challenge-fulfill request
        Request<ChallengeResponse> fulfillRequest = new Request<>(
                new Header(EndPointFunction.SIGN_IN_FULFILL_CHALLENGE),
                new ChallengeResponse(
                        fulfillChallenge(
                                challenge,
                                password
                        ),
                        sessionId
                )
        );

        // then the received challenge is fulfilled
        Response<SignInResponse> fulfillResponse = this.oneTimeNetworkResponseProvider.getSyncResponseFor(fulfillRequest);

        // the only exceptions that can occur are:
        //  - 500 Internal Server Error
        //  - 408 Timeout
        //  - 401 Unauthorized
        if (fulfillResponse.getError() != null) {
            int errorCode = requestResponse.getError().getErrorCode();

            if (errorCode == ResponseFields.Error.INTERNAL_SERVER_ERROR.getCode()) {
                throw new RemoteException();
            }
            else if (errorCode == ResponseFields.Error.TIMEOUT.getCode()) {
                return signIn(username, password);
            }

            return false; // 401 Unauthorized
        }

        // the token is saved and true is returned
        this.token = fulfillResponse.getBody().getToken();
        return true;
    }
}
