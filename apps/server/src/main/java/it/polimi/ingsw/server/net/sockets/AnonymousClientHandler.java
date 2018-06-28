package it.polimi.ingsw.server.net.sockets;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.server.net.sockets.middlewares.Middleware;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.server.utils.AuthenticationHelper;
import it.polimi.ingsw.server.utils.ServerLogger;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

public class AnonymousClientHandler extends ClientHandler {

    public AnonymousClientHandler(Socket client) throws IOException {
        super(client);
    }

    @Override
    public void run() {
        Middleware middleware;

        SocketAddress socketAddress = this.client.getRemoteSocketAddress();

        try {
            do {
                ServerLogger.getLogger()
                        .fine(() -> String.format(
                                "Waiting for data from client: %s",
                                socketAddress.toString()
                        ));

                JSONSerializable data = waitForData();

                ServerLogger.getLogger()
                        .fine(() -> String.format(
                                "Got data: \"%s\", from client: %s",
                                data.toString(),
                                socketAddress.toString()
                        ));

                // an anonymous client handler only accepts requests
                if (data instanceof Response) {
                    return;
                }

                Request<? extends JSONSerializable> request = (Request<? extends JSONSerializable>) data;

                // the first data received by an authenticated client handler is also a request
                if (tryMigration(request)) {
                    return;
                }

                ServerLogger.getLogger()
                        .fine(() -> String.format(
                                "Got request \"%s\", from client: %s",
                                request.toString(),
                                socketAddress.toString()
                        ));

                middleware = this.handleIncomingRequest(request);

                // if the middleware is null it means that the endpoint does not exists
                if (middleware == null) {
                    return;
                }

                // if the middleware needs the connection to be kept alive it wont be closed
            } while (middleware.shouldBeKeptAlive());
        }
        catch (Exception e) {
            ServerLogger.getLogger()
                    .log(Level.WARNING, "Error while handling client: " + socketAddress, e);
        }
    }
    
    /**
     * Tries to migrate from an {@link AnonymousClientHandler} to an {@link AuthenticatedClientHandler}.
     * It returns {@code true} if the migration was successful, {@code false} otherwise. If {@code true}
     * is returned the current {@link AnonymousClientHandler} should terminate.
     *
     * @param request the {@link Request} received which could contain a valid authentication token
     * @return {@code true} if the migration was successful, {@code false} otherwise
     * @throws SQLException if any SQL exceptions occurs
     * @throws IOException if any IO exceptions occurs
     */
    private boolean tryMigration(Request<?> request) throws SQLException, IOException {
        DatabasePlayer databasePlayer;

        try {
            databasePlayer = AuthenticationHelper.getAuthenticatedPlayer(request);
        }
        catch (TimeoutException e) {
            return false;
        }

        if (databasePlayer != null) {
            try (AuthenticatedClientHandler clientHandler = AuthenticatedClientHandler.migrate(
                    this,
                    databasePlayer,
                    request
            )) {
                ServerLogger.getLogger()
                        .finer(() -> String.format(
                                "Host %s migrated to AuthenticatedClientHandler, since it is authenticated as '%s'",
                                this.client.getRemoteSocketAddress().toString(),
                                databasePlayer.getUsername())
                        );

                clientHandler.run();
            }

            return true;
        }

        return false;
    }

    @Override
    public void close() throws IOException {
        this.client.close();
    }
}
