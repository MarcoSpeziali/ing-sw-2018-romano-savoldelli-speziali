package it.polimi.ingsw.server.net.sockets;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.server.net.commands.Command;
import it.polimi.ingsw.server.utils.ServerLogger;
import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
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

                ServerLogger.getLogger(AnonymousClientHandler.class)
                        .fine(() -> String.format(
                                "Got request \"%s\", from client: %s",
                                request.toString(),
                                socketAddress.toString()
                        ));

                // selects the handler for the request
                handler = SocketRouter.getHandlerForRequest(request);

                // if the handler is null it means that the endpoint does not exists
                if (handler == null) {
                    return;
                }

                // asks for a response
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

    @Override
    public void close() throws IOException {
        this.client.close();
    }
}
