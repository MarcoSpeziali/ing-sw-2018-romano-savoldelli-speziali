package it.polimi.ingsw.client.net.authentication;

import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.SignUpInterface;
import it.polimi.ingsw.net.utils.ResponseFields;
import it.polimi.ingsw.utils.CypherUtils;
import it.polimi.ingsw.utils.io.FilesUtils;
import it.polimi.ingsw.utils.io.Resources;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.GeneralSecurityException;

public class SignUpManager {
    private static SignUpManager instance = new SignUpManager();

    public static SignUpManager getManager() {
        return instance;
    }

    private SignUpInterface signUpInterface;

    private SignUpManager() {
        // TODO: set up signUpInterface
    }

    /**
     * @param username the username of the player
     * @param password the password of the player
     * @return {@code true} if the player successfully signed-up, {@code false} otherwise
     * @throws IOException if the server's public key could not be retrieved
     * @throws RemoteException if the operation could not be completed due to a connection or a server problem
     * @throws GeneralSecurityException if the password could not be encrypted with the server's public key
     */
    public boolean signUp(String username, String password) throws IOException, GeneralSecurityException {
        // encrypts the password with the server's public key
        String encryptedString = CypherUtils.encryptString(
                password,
                // gets the public key
                FilesUtils.getFileContent(Resources.getResource(getClass().getClassLoader(), "idra_rsa.pub")),
                false
        );

        // requests the sign-up
        Response response = this.signUpInterface.requestSignUp(username, encryptedString);

        // the possible errors are:
        //  - 409 Already Exists
        //  - 500 Internal Server Error
        if (response.getResponseError() != null) {
            int errorCode = response.getResponseError().getErrorCode();

            if (errorCode == ResponseFields.Error.ALREADY_EXISTS.getCode()) {
                return false;
            }
            else {
                throw new RemoteException();
            }
        }

        return true;
    }
}
