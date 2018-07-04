package it.polimi.ingsw.client.net;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.Settings;
import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.SignUpInterface;
import it.polimi.ingsw.client.net.providers.OneTimeNetworkResponseProvider;
import it.polimi.ingsw.client.net.providers.OneTimeRMIResponseProvider;
import it.polimi.ingsw.client.net.providers.OneTimeSocketResponseProvider;
import it.polimi.ingsw.net.requests.SignUpRequest;
import it.polimi.ingsw.net.responses.SignUpResponse;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.net.utils.ResponseFields;
import it.polimi.ingsw.utils.io.FilesUtils;
import it.polimi.ingsw.utils.text.CypherUtils;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.GeneralSecurityException;

public class SignUpManager {

    private static SignUpManager instance = new SignUpManager();
    
    private SignUpManager() {
    }

    public static SignUpManager getManager() {
        return instance;
    }

    /**
     * @param username the username of the player
     * @param password the password of the player
     * @return {@code true} if the player successfully signed-up, {@code false} otherwise
     * @throws IOException              if the server's public key could not be retrieved
     * @throws RemoteException          if the operation could not be completed due to a connection or a server problem
     * @throws GeneralSecurityException if the password could not be encrypted with the server's public key
     */
    public boolean signUp(String username, String password) throws IOException, GeneralSecurityException, NotBoundException {
        OneTimeNetworkResponseProvider oneTimeNetworkResponseProvider;
        
        if (Settings.getSettings().getProtocol().equals(Constants.Protocols.SOCKETS)) {
            oneTimeNetworkResponseProvider = new OneTimeSocketResponseProvider(
                    Settings.getSettings().getServerAddress(),
                    Settings.getSettings().getServerSocketPort()
            );
        }
        else {
            oneTimeNetworkResponseProvider = new OneTimeRMIResponseProvider<>(
                    Settings.getSettings().getServerAddress(),
                    Settings.getSettings().getServerRMIPort(),
                    SignUpInterface.class
            );
        }
        
        // encrypts the password with the server's public key
        String encryptedString = CypherUtils.encryptString(
                password,
                FilesUtils.getFileContentAsBytes(Constants.Resources.IDRA_PUBLIC_KEY.getURL()),
                false
        );

        // builds the sign-up request
        Request<SignUpRequest> signUpRequest = new Request<>(
                new Header(EndPointFunction.SIGN_UP),
                new SignUpRequest(
                        username,
                        encryptedString
                )
        );

        // requests the sign-up
        Response<SignUpResponse> response = oneTimeNetworkResponseProvider.getSyncResponseFor(signUpRequest);

        // the possible errors are:
        //  - 409 Already Exists
        //  - 500 Internal Server Error
        if (response.getError() != null) {
            int errorCode = response.getError().getErrorCode();

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
