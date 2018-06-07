package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.server.sql.DatabaseSession;

import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public final class AuthenticationManager {

    private static AuthenticationManager instance = new AuthenticationManager();

    public static AuthenticationManager getInstance() {
        return instance;
    }

    // TODO: complete
    public static synchronized DatabasePlayer getAuthenticatedPlayer(Request request) throws SQLException, TimeoutException {
        if (request.getHeader().getClientToken() == null) {
            return null; // TODO: throw malformed request exception
        }

        String token = request.getHeader().getClientToken();

        DatabaseSession session = DatabaseSession.sessionWithToken(token);

        if (session == null) {
            return null; // TODO: throw exception
        }
        else if (session.getInvalidationTimeStamp() < System.currentTimeMillis()) {
            throw new TimeoutException(); // TODO: create custom exception
        }

        return session.getPreAuthenticationSession().getPlayer();
    }
}
