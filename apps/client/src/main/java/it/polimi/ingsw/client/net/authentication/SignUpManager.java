package it.polimi.ingsw.client.net.authentication;

import it.polimi.ingsw.client.Settings;
import it.polimi.ingsw.client.net.providers.OneTimeNetworkResponseProvider;
import it.polimi.ingsw.client.net.providers.OneTimeRMIResponseProvider;
import it.polimi.ingsw.client.net.providers.OneTimeSocketResponseProvider;
import it.polimi.ingsw.net.*;
import it.polimi.ingsw.net.interfaces.SignUpInterface;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.net.utils.RequestFields;
import it.polimi.ingsw.net.utils.ResponseFields;
import it.polimi.ingsw.utils.CypherUtils;
import it.polimi.ingsw.utils.io.FilesUtils;
import it.polimi.ingsw.utils.io.Resources;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.GeneralSecurityException;
import java.util.Map;

public class SignUpManager {
    private static SignUpManager instance = new SignUpManager();

    public static SignUpManager getManager() {
        return instance;
    }

    private OneTimeNetworkResponseProvider oneTimeNetworkResponseProvider;

    private SignUpManager() {
        if (Settings.getSettings().isUsingSockets()) {
            oneTimeNetworkResponseProvider = new OneTimeSocketResponseProvider();
        }
        else {
            oneTimeNetworkResponseProvider = new OneTimeRMIResponseProvider<>(SignUpInterface.class);
        }
    }

    /**
     * @param username the username of the player
     * @param password the password of the player
     * @return {@code true} if the player successfully signed-up, {@code false} otherwise
     * @throws IOException if the server's public key could not be retrieved
     * @throws RemoteException if the operation could not be completed due to a connection or a server problem
     * @throws GeneralSecurityException if the password could not be encrypted with the server's public key
     */
    public boolean signUp(String username, String password) throws IOException, GeneralSecurityException, NotBoundException, ReflectiveOperationException {
        // encrypts the password with the server's public key
        String encryptedString = CypherUtils.encryptString(
                password,
                // gets the public key
                FilesUtils.getFileContent(Resources.getResource(getClass().getClassLoader(), "idra_rsa.pub")),
                false
        );

        // builds the sign-up request
        Request signUpRequest = new Request(
                new RequestHeader(ClientMachineInfo.generate(), null),
                new Body(
                        EndPointFunction.SIGN_UP,
                        Map.of(
                                RequestFields.Authentication.USERNAME.getFieldName(), username,
                                RequestFields.Authentication.PASSWORD.getFieldName(), encryptedString
                        )
                )
        );

        // requests the sign-up
        Response response = this.oneTimeNetworkResponseProvider.getSyncResponseFor(signUpRequest);

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
