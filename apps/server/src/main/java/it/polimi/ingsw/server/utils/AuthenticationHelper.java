package it.polimi.ingsw.server.utils;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.server.sql.DatabaseSession;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public final class AuthenticationHelper {

    private AuthenticationHelper() {}

    /**
     * Gets the {@link DatabasePlayer} that made the {@link Request} if the token is present and valid.
     *
     * @param request the {@link Request} made by the {@link DatabasePlayer}
     * @return the {@link DatabasePlayer} that made the {@link Request} if the token is present and valid or {@literal null} if the token is invalid.
     * @throws SQLException if any sql exception occurs
     * @throws TimeoutException if the token is not {@literal null} but it is expired
     */
    public static synchronized DatabasePlayer getAuthenticatedPlayer(Request<? extends JSONSerializable> request) throws SQLException, TimeoutException {
        return getAuthenticatedPlayer(request.getHeader().getClientToken());
    }

    /**
     * Gets the {@link DatabasePlayer} associated with the token of a {@link DatabaseSession}.
     *
     * @param token the token associated to the {@link DatabasePlayer}
     * @return the {@link DatabasePlayer} associated with the token of a {@link DatabaseSession} or {@literal null} if the token is invalid.
     * @throws SQLException if any sql exception occurs
     * @throws TimeoutException if the token is not {@literal null} but it is expired
     */
    public static synchronized DatabasePlayer getAuthenticatedPlayer(String token) throws SQLException, TimeoutException {
        DatabaseSession session = DatabaseSession.sessionWithToken(token);

        // session not existing
        if (session == null) {
            return null;
        }
        // session expired
        else if (session.getInvalidationTimeStamp() < System.currentTimeMillis()) {
            throw new TimeoutException();
        }

        return session.getPreAuthenticationSession().getPlayer();
    }

    /**
     * @param request the {@link Request} made by the {@link DatabasePlayer}
     * @return whether the {@link Request} comes from an authenticated {@link DatabasePlayer} with an active {@link DatabaseSession}
     * @throws SQLException if any sql exception occurs
     */
    public static synchronized boolean isAuthenticated(Request<? extends JSONSerializable> request) throws SQLException {
        if (request.getHeader().getClientToken() == null) {
            return false;
        }

        String token = request.getHeader().getClientToken();

        DatabaseSession session = DatabaseSession.sessionWithToken(token);

        return session != null && session.getInvalidationTimeStamp() >= System.currentTimeMillis();
    }
}
