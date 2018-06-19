package it.polimi.ingsw.server.net.sockets;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.server.net.commands.Command;
import it.polimi.ingsw.server.utils.ServerLogger;
import it.polimi.ingsw.utils.io.json.JSONSerializable;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.sql.SQLException;
import java.util.logging.Level;

// TODO: docs
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
     *
     * @param bufferedReader the {@link BufferedReader} which reads from the client
     * @return the {@link Request} created by the client
     * @throws IOException if any IO error occurs
     */
    protected Request waitForRequest(BufferedReader bufferedReader) throws IOException {
        String content = bufferedReader.readLine();

        if (content == null) {
            throw new IOException();
        }

        //noinspection unchecked
        return JSONSerializable.deserialize(Request.class, content);
    }

    /**
     * Sends a {@link Response} to the client.
     *
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

    protected Command handleAnonymousIncomingRequest() throws IOException, SQLException {
        return handleGenericRequest(null, SocketRouter::getHandlerForAnonymousRequest);
    }

    protected Command handleIncomingRequest(CanContinueMiddleware<Request<? extends JSONSerializable>> shouldHandleRequestFunction) throws IOException, SQLException {
        return handleGenericRequest(shouldHandleRequestFunction, SocketRouter::getHandlerForRequest);
    }

    private Command handleGenericRequest(CanContinueMiddleware<Request<? extends JSONSerializable>> shouldHandleRequestFunction, HandlerChooserMiddleware commandGetter) throws IOException, SQLException {
        SocketAddress socketAddress = this.client.getRemoteSocketAddress();

        // waits for a request
        @SuppressWarnings("unchecked") Request<? extends JSONSerializable> request = waitForRequest(this.in);

        ServerLogger.getLogger()
                .fine(() -> String.format(
                        "Got request \"%s\", from client: %s",
                        request.toString(),
                        socketAddress.toString()
                ));

        return handleRequest(socketAddress.toString(), request, shouldHandleRequestFunction, commandGetter);
    }

    protected Command handleMigrationRequest(Request<? extends JSONSerializable> request, CanContinueMiddleware<Request<? extends JSONSerializable>> shouldHandleRequestFunction) throws IOException, SQLException {
        return handleRequest(
                this.client.getRemoteSocketAddress().toString(),
                request,
                shouldHandleRequestFunction,
                SocketRouter::getHandlerForRequest
        );
    }

    private Command handleRequest(String socketAddress, Request<? extends JSONSerializable> request, CanContinueMiddleware<Request<? extends JSONSerializable>> shouldHandleRequestFunction, HandlerChooserMiddleware commandGetter) throws IOException, SQLException {
        ServerLogger.getLogger()
                .fine(() -> String.format(
                        "Got request \"%s\", from client: %s",
                        request.toString(),
                        socketAddress
                ));

        try {
            if (shouldHandleRequestFunction != null && !shouldHandleRequestFunction.canContinue(request)) {
                return null;
            }
        }
        catch (SQLException e) {
            ServerLogger.getLogger().log(Level.SEVERE, "Error while querying the database", e);

            return null;
        }

        // selects the handler for the request
        Command handler = commandGetter.getHandler(request);

        // if the handler is null it means that the endpoint does not exists
        if (handler == null) {
            ServerLogger.getLogger()
                    .warning(() -> String.format("Cannot handle request with endpoint: %s, it does not exists.", request.getHeader().getEndPointFunction().toString()));
            return null;
        }

        // asks for a response
        @SuppressWarnings("unchecked")
        Response<? extends JSONSerializable> response = handler.handle(request, this.client);

        ServerLogger.getLogger()
                .fine(() -> String.format(
                        "Sending response \"%s\", to client: %s", response.toString(), socketAddress
                ));

        // and sends it
        this.sendResponse(response);

        return handler;
    }

    @FunctionalInterface
    public interface CanContinueMiddleware<T> {
        boolean canContinue(T value) throws SQLException, IOException;
    }

    private interface HandlerChooserMiddleware {
        <T extends JSONSerializable> Command getHandler(Request<T> request) throws SQLException;
    }
}
