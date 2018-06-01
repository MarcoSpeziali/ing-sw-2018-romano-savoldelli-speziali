package it.polimi.ingsw.client.net;

import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.ResponseError;
import it.polimi.ingsw.net.interfaces.SignInInterface;

import java.rmi.RemoteException;
import java.util.function.Consumer;

// TODO: document
public class LoginManager {
    private static LoginManager instance = new LoginManager();

    public static LoginManager sharedInstance() {
        return instance;
    }

    private String token;
    private SignInInterface loginInterface;

    private LoginManager() {
        // TODO: set login interface
    }

    public boolean isAuthenticated() {
        return this.token != null;
    }

    public void authenticate(String username, String password, Consumer<ResponseError> loginError) throws RemoteException {
        Response challenge = loginInterface.requestLogin(username);
        Response token = loginInterface.fulfillChallenge(
                1, // response
                fulfillChallenge("..") // response
        );

        // se ok -> loginError == null
        // se ko -> loginError ...
    }

    private static String fulfillChallenge(String challenge) {
        // TODO: complete
        return challenge;
    }
}
