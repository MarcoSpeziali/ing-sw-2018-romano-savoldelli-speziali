package it.polimi.ingsw.server.net.endpoints;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.SignUpInterface;
import it.polimi.ingsw.net.requests.SignUpRequest;
import it.polimi.ingsw.net.responses.SignUpResponse;
import it.polimi.ingsw.server.Constants;
import it.polimi.ingsw.server.net.ResponseFactory;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.server.utils.ServerLogger;
import it.polimi.ingsw.utils.io.FilesUtils;
import it.polimi.ingsw.utils.text.CypherUtils;
import it.polimi.ingsw.utils.text.HashUtils;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.logging.Level;

public class SignUpEndPoint extends UnicastRemoteObject implements SignUpInterface {

    private static final long serialVersionUID = -1919251653537403377L;

    public SignUpEndPoint() throws RemoteException {
    }

    @Override
    public Response<SignUpResponse> requestSignUp(Request<SignUpRequest> request) {
        String username = request.getBody().getUsername();
        String encryptedPassword = request.getBody().getPassword();

        try {
            // gets the player with the provided username
            DatabasePlayer player = DatabasePlayer.playerWithUsername(username);

            if (player != null) {
                return ResponseFactory.createAlreadyExistsError(request);
            }

        }
        catch (SQLException e) {
            ServerLogger.getLogger(SignUpEndPoint.class).log(Level.SEVERE, "Error while querying the database", e);

            return ResponseFactory.createInternalServerError(request);
        }

        try {
            // decrypts the password sent by the user whit the server private key
            String password = CypherUtils.decryptString(
                    encryptedPassword,
                    FilesUtils.getFileContentAsBytes(Constants.Resources.PRIVATE_KEY.getURL()),
                    true
            );

            // then the password is hashed with sha1
            password = HashUtils.sha1(password);

            // finally the player is saved into the database
            DatabasePlayer.insertPlayer(username, password);
        }
        catch (IOException | GeneralSecurityException e) {
            ServerLogger.getLogger(SignUpEndPoint.class).log(Level.SEVERE, "Error while decrypting the password", e);

            return ResponseFactory.createInternalServerError(request);
        }
        catch (SQLException e) {
            ServerLogger.getLogger(SignUpEndPoint.class).log(Level.SEVERE, "Error while querying the database", e);

            return ResponseFactory.createInternalServerError(request);
        }

        // finally sends back the result
        return ResponseFactory.createUserCreatedResponse(request);
    }
}
