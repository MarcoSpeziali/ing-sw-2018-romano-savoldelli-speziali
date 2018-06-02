package it.polimi.ingsw.server.net;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.server.utils.ServerLogger;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Level;

public class ClientHandler implements Runnable, AutoCloseable {

    private final Socket client;
    private final BufferedReader in;
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
                Request request = getRequest(this.in);
                handler = SocketRouter.getHandlerForRequest(request);

                if (handler == null) {
                    return;
                }

                Response response = handler.handle(request, this.client);
                sendResponse(response, this.out);
            } while (handler.shouldBeKeptAlive());
        } catch (Exception e) {
            ServerLogger.getLogger(ClientHandler.class)
                    .log(Level.WARNING, "Error while handling client: " + socketAddress, e);
        }
        finally {
            try {
                this.close();
            }
            catch (IOException e) {
                ServerLogger.getLogger(ClientHandler.class)
                        .log(Level.WARNING, "Error while closing connection with client: " + socketAddress, e);
            }
        }
    }

    @Override
    public void close() throws IOException {
        this.in.close();
        this.out.close();
        this.client.close();
    }

    private static Request getRequest(BufferedReader bufferedReader) {
        String content = bufferedReader.lines().reduce("", (s, s2) -> s + s2);

        Request request = new Request();
        request.deserialize(new JSONObject(content));

        return request;
    }

    private static void sendResponse(Response response, BufferedWriter bufferedWriter) throws IOException {
        JSONObject jsonObject = response.serialize();
        String jsonString = jsonObject.toString();

        bufferedWriter.write(jsonString);
        bufferedWriter.flush();
    }
}
