package it.polimi.ingsw.server.net.authentication;

import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.ResponseError;
import it.polimi.ingsw.net.interfaces.SignUpInterface;
import it.polimi.ingsw.server.Constants;
import it.polimi.ingsw.server.db.DatabasePlayer;
import it.polimi.ingsw.server.utils.ServerLogger;
import it.polimi.ingsw.utils.CypherUtils;
import it.polimi.ingsw.utils.HashUtils;
import it.polimi.ingsw.utils.io.FilesUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.logging.Level;

// TODO: docs
public class SignUpManager implements SignUpInterface {

    @Override
    public Response requestSignUp(String username, String encryptedPassword) {
        try {
            DatabasePlayer player = DatabasePlayer.playerWithUsername(username);

            if (player != null) {
                return new Response(
                        new ResponseError(
                                409,
                                "already-exists"
                        )
                );
            }

        } catch (SQLException e) {
            ServerLogger.getLogger(SignInManager.class).log(Level.SEVERE, "Error while querying the database", e);

            return new Response(
                    new ResponseError(
                            500,
                            "internal-server-error"
                    )
            ); // server error
        }

        try {
            String password = CypherUtils.decryptString(
                    encryptedPassword,
                    FilesUtils.getFileContent(Constants.Paths.SSH_KEY_PATH.getAbsolutePath()),
                    true
            );

            password = HashUtils.sha1(password);

            DatabasePlayer.insertPlayer(username, password);
        } catch (IOException | GeneralSecurityException e) {
            ServerLogger.getLogger(SignInManager.class).log(Level.SEVERE, "Error while decrypting the password", e);

            return new Response(
                    new ResponseError(
                            500,
                            "internal-server-error"
                    )
            ); // server error
        } catch (SQLException e) {
            ServerLogger.getLogger(SignInManager.class).log(Level.SEVERE, "Error while querying the database", e);

            return new Response(
                    new ResponseError(
                            500,
                            "internal-server-error"
                    )
            ); // server error
        }

        return new Response(); // empty response
    }
}
