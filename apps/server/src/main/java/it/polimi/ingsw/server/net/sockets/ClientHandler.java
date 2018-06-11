package it.polimi.ingsw.server.net.sockets;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.server.managers.AuthenticationManager;
import it.polimi.ingsw.server.net.commands.Command;
import it.polimi.ingsw.server.net.endpoints.SignInEndPoint;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.server.utils.ServerLogger;
import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.logging.Level;

public abstract class ClientHandler implements Runnable, AutoCloseable {

    /**
     * The {@link Socket} of the connected client.
     */
    protected final Socket client;

    /**
     * The {@link BufferedReader} of the client's connection.
     */
    protected final BufferedReader in;

    /**
     * The {@link BufferedWriter} of the client's connection.
     */
    protected final BufferedWriter out;

    public ClientHandler(Socket client) throws IOException {
        this.client = client;
        this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
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
    protected Request<? extends JSONSerializable> waitForRequest(BufferedReader bufferedReader) throws IOException {
        String content = bufferedReader.readLine();

        Request<? extends JSONSerializable> request = new Request<>();
        request.deserialize(new JSONObject(content));

        return request;
    }

    /**
     * Sends a {@link Response} to the client.
     * @param response sends a {@link Response} to the connected client
     * @throws IOException if any IO error occurs
     */
    public void sendResponse(Response<? extends JSONSerializable> response) throws IOException {
        JSONObject jsonObject = response.serialize();
        String jsonString = jsonObject.toString();

        this.out.write(jsonString);
        this.out.newLine();
        this.out.flush();
    }
    
    protected Command handleAnonymousIncomingRequest(Consumer<Request> migrateToAuthenticatedHandler) throws IOException {
        return handleIncomingRequest(null, migrateToAuthenticatedHandler);
    }
    
    protected Command handleIncomingRequest(Middleware<Request<? extends JSONSerializable>> shouldHandleRequestFunction, Consumer<Request> migrateToAuthenticatedHandler) throws IOException {
        SocketAddress socketAddress = this.client.getRemoteSocketAddress();
        
        // waits for a request
        Request<? extends JSONSerializable> request = waitForRequest(this.in);
    
        try {
            DatabasePlayer databasePlayer = AuthenticationManager.getAuthenticatedPlayer(request);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (TimeoutException e) {
            e.printStackTrace();
        }
    
        ServerLogger.getLogger(AnonymousClientHandler.class)
                .fine(() -> String.format(
                        "Got request \"%s\", from client: %s",
                        request.toString(),
                        socketAddress.toString()
                ));
    
        try {
            if (shouldHandleRequestFunction != null && !shouldHandleRequestFunction.canContinue(request)) {
                return null;
            }
        }
        catch (SQLException e) {
            ServerLogger.getLogger(SignInEndPoint.class).log(Level.SEVERE, "Error while querying the database", e);
            
            return null;
        }
    
        // selects the handler for the request
        Command handler = SocketRouter.getHandlerForAnonymousRequest(request);
        
        // if the handler is null it means that the endpoint does not exists
        if (handler == null) {
            return null;
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
        
        return handler;
    }
    
    @FunctionalInterface
    public interface Middleware<T> {
        boolean canContinue(T value) throws SQLException, IOException;
    }
}
