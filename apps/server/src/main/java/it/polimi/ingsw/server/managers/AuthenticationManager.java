package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.server.sql.DatabaseSession;
import it.polimi.ingsw.utils.io.JSONSerializable;

import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public final class AuthenticationManager {

    private AuthenticationManager() {
    }

    // TODO: complete
    public static synchronized DatabasePlayer getAuthenticatedPlayer(Request<? extends JSONSerializable> request) throws SQLException, TimeoutException {
        return getAuthenticatedPlayer(request.getHeader().getClientToken());
    }

    public static synchronized DatabasePlayer getAuthenticatedPlayer(String token) throws SQLException, TimeoutException {
        DatabaseSession session = DatabaseSession.sessionWithToken(token);

        if (session == null) {
            return null;
        }
        else if (session.getInvalidationTimeStamp() < System.currentTimeMillis()) {
            throw new TimeoutException();
        }

        return session.getPreAuthenticationSession().getPlayer();
    }

    public static synchronized boolean isAuthenticated(Request<? extends JSONSerializable> request) throws SQLException {
        if (request.getHeader().getClientToken() == null) {
            return false;
        }

        String token = request.getHeader().getClientToken();

        DatabaseSession session = DatabaseSession.sessionWithToken(token);

        return session != null && session.getInvalidationTimeStamp() >= System.currentTimeMillis();
    }
}
