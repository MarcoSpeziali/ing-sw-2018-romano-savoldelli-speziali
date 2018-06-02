package it.polimi.ingsw.client.net.authentication;

import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.SignInInterface;
import it.polimi.ingsw.net.utils.ResponseFields;
import it.polimi.ingsw.utils.HashUtils;

import java.rmi.RemoteException;
import java.util.concurrent.TimeoutException;

// TODO: docs
public class SignInManager {

    private static SignInManager instance = new SignInManager();

    public static SignInManager getManager() {
        return instance;
    }

    private String token;
    private SignInInterface signInInterface;

    private SignInManager() {
        // TODO: set up signInInterface
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
    public boolean signIn(String username, String password) throws RemoteException, TimeoutException {
        // first a login request is made
        Response requestResponse = this.signInInterface.requestLogin(username);

        // the only possible error that can occur is a 500 Internal Server Error
        if (requestResponse.getResponseError() != null) {
            throw new RemoteException();
        }

        // then the received challenge is fulfilled
        Response fulfillResponse = this.signInInterface.fulfillChallenge(
                // gets the session id
                (int) requestResponse.getBody().get(ResponseFields.Authentication.SESSION_ID.getFieldName()),
                fulfillChallenge(
                        // gets the challenge
                        (String) requestResponse.getBody().get(ResponseFields.Authentication.CHALLENGE.getFieldName()),
                        password
                )
        );

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
        this.token = (String) fulfillResponse.getBody().get(ResponseFields.Authentication.TOKEN.getFieldName());
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
