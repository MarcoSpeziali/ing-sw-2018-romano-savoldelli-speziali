package it.polimi.ingsw.server.net.endpoints;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.SignUpInterface;
import it.polimi.ingsw.net.utils.RequestFields;
import it.polimi.ingsw.server.Constants;
import it.polimi.ingsw.server.net.ResponseFactory;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.server.utils.ServerLogger;
import it.polimi.ingsw.utils.CypherUtils;
import it.polimi.ingsw.utils.HashUtils;
import it.polimi.ingsw.utils.io.FilesUtils;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.logging.Level;

public class SignUpEndPoint extends UnicastRemoteObject implements SignUpInterface {

    private static final long serialVersionUID = -1919251653537403377L;

    private static SignUpEndPoint instance;

    static {
        try {
            instance = new SignUpEndPoint();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public SignUpEndPoint() throws RemoteException {
    }

    public static SignUpEndPoint getInstance() {
        return instance;
    }

    @Override
    public Response requestSignUp(Request request) {
        String username = (String) request.getRequestBody().get(RequestFields.Authentication.USERNAME.getFieldName());
        String encryptedPassword = (String) request.getRequestBody().get(RequestFields.Authentication.PASSWORD.getFieldName());

        try {
            // gets the player with the provided username
            DatabasePlayer player = DatabasePlayer.playerWithUsername(username);

            if (player != null) {
                return ResponseFactory.createAlreadyExistsError();
            }

        } catch (SQLException e) {
            ServerLogger.getLogger(SignUpEndPoint.class).log(Level.SEVERE, "Error while querying the database", e);

            return ResponseFactory.createInternalServerError();
        }

        try {
            // decrypts the password sent by the user whit the server private key
            String password = CypherUtils.decryptString(
                    encryptedPassword,
                    FilesUtils.getFileContent(Constants.Paths.SSH_KEY_PATH.getAbsolutePath()),
                    true
            );

            // then the password is hashed with sha1
            password = HashUtils.sha1(password);

            // finally the player is saved into the database
            DatabasePlayer.insertPlayer(username, password);
        } catch (IOException | GeneralSecurityException e) {
            ServerLogger.getLogger(SignUpEndPoint.class).log(Level.SEVERE, "Error while decrypting the password", e);

            return ResponseFactory.createInternalServerError();
        } catch (SQLException e) {
            ServerLogger.getLogger(SignUpEndPoint.class).log(Level.SEVERE, "Error while querying the database", e);

            return ResponseFactory.createInternalServerError();
        }

        // finally sends back the result
        return ResponseFactory.createUserCreatedResponse(request);
    }
}
