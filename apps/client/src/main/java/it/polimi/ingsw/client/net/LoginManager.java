package it.polimi.ingsw.client.net;

import it.polimi.ingsw.net.AuthenticationError;
import it.polimi.ingsw.net.AuthenticationResponse;
import it.polimi.ingsw.net.authentication.AuthenticationInterface;

import java.rmi.RemoteException;
import java.util.function.Consumer;

// TODO: document
public class LoginManager {
    private static LoginManager instance = new LoginManager();

    public static LoginManager sharedInstance() {
        return instance;
    }

    private String token;
    private AuthenticationInterface loginInterface;

    private LoginManager() {
        // TODO: set login interface
    }

    public boolean isAuthenticated() {
        return this.token != null;
    }

    public void authenticate(String username, String password, Consumer<AuthenticationError> authError) throws RemoteException {
        AuthenticationResponse challenge = loginInterface.requestLogin(username);
        AuthenticationResponse token = loginInterface.fulfillChallenge(
                "..", // response
                fulfillChallenge("..") // response
        );

        // se ok -> authError == null
        // se ko -> authError ...
    }

    private static String fulfillChallenge(String challenge) {
        // TODO: complete
        return challenge;
    }
}
