package it.polimi.ingsw.client.net.authentication;

import it.polimi.ingsw.client.Settings;
import it.polimi.ingsw.client.net.providers.OneTimeNetworkResponseProvider;
import it.polimi.ingsw.client.net.providers.OneTimeRMIResponseProvider;
import it.polimi.ingsw.client.net.providers.OneTimeSocketResponseProvider;
import it.polimi.ingsw.net.*;
import it.polimi.ingsw.net.interfaces.SignInInterface;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.net.utils.RequestFields;
import it.polimi.ingsw.net.utils.ResponseFields;
import it.polimi.ingsw.utils.HashUtils;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class SignInManager {

    private static SignInManager instance = new SignInManager();

    public static SignInManager getManager() {
        return instance;
    }

    private String token;
    private OneTimeNetworkResponseProvider oneTimeNetworkResponseProvider;

    private SignInManager() {
        if (Settings.getSettings().isUsingSockets()) {
            oneTimeNetworkResponseProvider = new OneTimeSocketResponseProvider();
        }
        else {
            oneTimeNetworkResponseProvider = new OneTimeRMIResponseProvider<>(SignInInterface.class);
        }
    }

    /**
     * @return whether the user is currently authenticated
     */
    public boolean isAuthenticated() {
        return this.token != null;
    }

    /**
     * Authenticates the user.
     * @param username the username of the player
     * @param password the password of the player
     * @return {@code true} if the user has been successfully authenticated, {@code false} otherwise
     * @throws RemoteException if a server error or a connection error occurred
     * @throws TimeoutException if the challenge haven't been completed in time
     */
    public boolean signIn(String username, String password) throws IOException, TimeoutException, NotBoundException, ReflectiveOperationException {
        // builds the sign-in request
        Request authenticationRequest = new Request(
                new RequestHeader(null),
                new Body(
                        EndPointFunction.REQUEST_AUTHENTICATION,
                        Map.of(RequestFields.Body.Authentication.USERNAME.toString(), username)
                )
        );

        // first a login request is made
        Response requestResponse = this.oneTimeNetworkResponseProvider.getSyncResponseFor(authenticationRequest);

        // the only possible error that can occur is a 500 Internal Server Error
        if (requestResponse.getResponseError() != null) {
            throw new RemoteException();
        }

        int sessionId = (int) requestResponse.getBody().get(
                ResponseFields.Body.Authentication.SESSION_ID.toString()
        );
        String challenge = (String) requestResponse.getBody().get(
                ResponseFields.Body.Authentication.CHALLENGE.toString()
        );

        // builds the challenge-fulfill request
        Request fulfillRequest = new Request(
                new RequestHeader(null),
                new Body(
                        EndPointFunction.FULFILL_AUTHENTICATION_CHALLENGE,
                        Map.of(
                                RequestFields.Body.Authentication.SESSION_ID.toString(), sessionId,
                                RequestFields.Body.Authentication.CHALLENGE_RESPONSE.toString(), fulfillChallenge(
                                        challenge,
                                        password
                                )
                        )
                )
        );

        // then the received challenge is fulfilled
        Response fulfillResponse = this.oneTimeNetworkResponseProvider.getSyncResponseFor(fulfillRequest);

        // the only exceptions that can occur are:
        //  - 500 Internal Server Error
        //  - 408 Timeout
        //  - 401 Unauthorized
        if (fulfillResponse.getResponseError() != null) {
            int errorCode = requestResponse.getResponseError().getErrorCode();

            if (errorCode == ResponseFields.Error.INTERNAL_SERVER_ERROR.getCode()) {
                throw new RemoteException();
            }
            else if (errorCode == ResponseFields.Error.TIMEOUT.getCode()) {
                throw new TimeoutException();
            }

            return false; // 401 Unauthorized
        }

        // the token is saved and true is returned
        this.token = (String) fulfillResponse.getBody().get(ResponseFields.Body.Authentication.TOKEN.toString());
        return true;
    }

    /**
     * Fulfills the challenge.
     * @param challenge the challenge to fulfill
     * @param password the user password
     * @return the fulfilled challenge
     */
    private static String fulfillChallenge(String challenge, String password) {
        password = HashUtils.sha1(password);

        return HashUtils.sha1(challenge + password);
    }
}
