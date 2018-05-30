package it.polimi.ingsw.client.net;

import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.ResponseError;
import it.polimi.ingsw.net.interfaces.SignUpInterface;
import it.polimi.ingsw.net.utils.EncryptUtil;
import java.rmi.RemoteException;
import java.util.function.Consumer;

public class SignUpManager {
    private static SignUpManager instance = new SignUpManager();

    public static SignUpManager sharedInstance() {
        return instance;
    }

    private SignUpInterface signUpInterface;

    private SignUpManager() {
        // TODO: set signUp interface
    }

    public void signUp(String username, String password, Consumer<ResponseError> signUpError) throws RemoteException {
        Response response = signUpInterface.requestSignUp(username, EncryptUtil.encryptPassword(password));
        LoginManager.sharedInstance().authenticate(username, password, null);

        // se ok -> signupError == null
        // se ko -> signuError ...
    }
}




