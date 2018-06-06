package it.polimi.ingsw.server.net;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.server.net.handlers.CommandHandler;
import it.polimi.ingsw.server.utils.ServerLogger;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Level;

public class ClientHandler implements Runnable, AutoCloseable {

    /**
     * The {@link Socket} of the connected client.
     */
    private final Socket client;

    /**
     * The {@link BufferedReader} of the client's connection.
     */
    private final BufferedReader in;

    /**
     * The {@link BufferedWriter} of the client's connection.
     */
    private final BufferedWriter out;

    public ClientHandler(Socket client) throws IOException {
        this.client = client;
        this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
    }

    @Override
    public void run() {
        CommandHandler handler;

        SocketAddress socketAddress = this.client.getRemoteSocketAddress();

        try {
            do {
                // waits for a request
                Request request = waitForRequest(this.in);

                ServerLogger.getLogger(ClientHandler.class)
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
                Response response = handler.handle(request, this.client);

                ServerLogger.getLogger(ClientHandler.class)
                        .fine(() -> String.format(
                                "Sending response \"%s\", to client: %s", response.toString(), socketAddress.toString()
                        ));

                // and sends it
                sendResponse(response, this.out);

                // if the handler needs the connection to be kept alive it wont be closed
            } while (handler.shouldBeKeptAlive());
        }
        catch (Exception e) {
            ServerLogger.getLogger(ClientHandler.class)
                    .log(Level.WARNING, "Error while handling client: " + socketAddress, e);
        }
    }

    @Override
    public void close() throws IOException {
        this.client.close();
    }

    /**
     * Waits for the client to send a {@link Request}.
     * @param bufferedReader the {@link BufferedReader} which reads from the client
     * @return the {@link Request} created by the client
     * @throws IOException if any IO error occurs
     */
    private static Request waitForRequest(BufferedReader bufferedReader) throws IOException {
        String content = bufferedReader.readLine();

        Request request = new Request();
        request.deserialize(new JSONObject(content));

        return request;
    }

    private static void sendResponse(Response response, BufferedWriter bufferedWriter) throws IOException {
        JSONObject jsonObject = response.serialize();
        String jsonString = jsonObject.toString();

        bufferedWriter.write(jsonString);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }
}
