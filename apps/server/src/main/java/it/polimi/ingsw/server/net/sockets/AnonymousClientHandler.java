package it.polimi.ingsw.server.net.sockets;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.server.Constants;
import it.polimi.ingsw.server.managers.AuthenticationManager;
import it.polimi.ingsw.server.net.commands.Command;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.server.utils.ServerLogger;
import it.polimi.ingsw.utils.io.JSONSerializable;

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
        Command handler;

        SocketAddress socketAddress = this.client.getRemoteSocketAddress();

        try {
            do {
                // waits for a request
                Request<? extends JSONSerializable> request = waitForRequest(this.in);

                if (tryMigration(request)) {
                    return;
                }

                ServerLogger.getLogger(AnonymousClientHandler.class)
                        .fine(() -> String.format(
                                "Got request \"%s\", from client: %s",
                                request.toString(),
                                socketAddress.toString()
                        ));

                // selects the handler for the request
                handler = SocketRouter.getHandlerForAnonymousRequest(request);

                // if the handler is null it means that the endpoint does not exists
                if (handler == null) {
                    return;
                }

                // asks for a response
                @SuppressWarnings("unchecked")
                Response<? extends JSONSerializable> response = handler.handle(request, this.client);

                ServerLogger.getLogger(AnonymousClientHandler.class)
                        .fine(() -> String.format(
                                "Sending response \"%s\", to client: %s", response.toString(), socketAddress.toString()
                        ));

                // and sends it
                this.sendResponse(response);

                // if the handler needs the connection to be kept alive it wont be closed
            } while (handler.shouldBeKeptAlive());
        }
        catch (Exception e) {
            ServerLogger.getLogger(AnonymousClientHandler.class)
                    .log(Level.WARNING, "Error while handling client: " + socketAddress, e);
        }
    }

    // TODO: docs
    private boolean tryMigration(Request<?> request) throws SQLException, TimeoutException, IOException {
        DatabasePlayer databasePlayer = AuthenticationManager.getAuthenticatedPlayer(request);

        if (databasePlayer != null) {
            AuthenticatedClientHandler clientHandler = AuthenticatedClientHandler.migrate(
                    this,
                    databasePlayer,
                    request
            );

            new Thread(
                    clientHandler,
                    Constants.Threads.PLAYER_HANDLER + "-" + clientHandler.getPlayer().toString()
            ).start();

            return true;
        }

        return false;
    }

    @Override
    public void close() throws IOException {
        this.client.close();
    }
}
