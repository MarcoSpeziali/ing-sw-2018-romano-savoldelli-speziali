package it.polimi.ingsw.server.net.sockets;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.responses.NullResponse;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.net.utils.RequestFields;
import it.polimi.ingsw.net.utils.ResponseFields;
import it.polimi.ingsw.server.net.sockets.middlewares.Handles;
import it.polimi.ingsw.server.net.sockets.middlewares.Middleware;
import it.polimi.ingsw.server.net.sockets.middlewares.factories.MiddlewareFactory;
import it.polimi.ingsw.server.utils.ServerLogger;
import it.polimi.ingsw.server.utils.io.JSONBufferedReader;
import it.polimi.ingsw.server.utils.io.JSONBufferedWriter;
import it.polimi.ingsw.utils.ArrayUtils;
import it.polimi.ingsw.utils.io.json.JSONSerializable;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.EnumMap;
import java.util.Map;

public abstract class ClientHandler implements Runnable, AutoCloseable {

    protected static Map<EndPointFunction, MiddlewareFactory<? extends Middleware>> endPointFunctionMiddlewareFactoryEnumMap = new EnumMap<>(EndPointFunction.class);

    public static <T extends Middleware> void registerMiddleware(Class<T> middlewareClass, MiddlewareFactory<T> middlewareFactory) {
        Handles handles = middlewareClass.getAnnotation(Handles.class);

        if (handles == null || handles.value().length == 0) {
            return;
        }

        ArrayUtils.forEach(
                handles.value(),
                endPointFunction -> endPointFunctionMiddlewareFactoryEnumMap.put(endPointFunction, middlewareFactory)
        );
    }

    /**
     * The {@link Socket} of the connected client.
     */
    protected final Socket client;

    /**
     * The {@link BufferedReader} of the client's connection.
     */
    protected final JSONBufferedReader in;

    /**
     * The {@link BufferedWriter} of the client's connection.
     */
    protected final JSONBufferedWriter out;

    protected final Map<EndPointFunction, Middleware> dynamicMiddlewares = new EnumMap<>(EndPointFunction.class);

    public void addMiddleware(Class<?> middlewareClass, Middleware middleware) {
        Handles handles = middlewareClass.getAnnotation(Handles.class);

        if (handles == null || handles.value().length == 0) {
            return;
        }

        ArrayUtils.forEach(
                handles.value(),
                endPointFunction -> dynamicMiddlewares.put(endPointFunction, middleware)
        );
    }

    public ClientHandler(Socket client) throws IOException {
        this.client = client;
        this.in = new JSONBufferedReader(new InputStreamReader(client.getInputStream()));
        this.out = new JSONBufferedWriter(new OutputStreamWriter(client.getOutputStream()), true);
    }

    @Override
    public void close() throws IOException {
        this.client.close();
    }

    protected JSONSerializable waitForData() throws IOException {
        do {
            JSONObject jsonObject = this.in.readJSON();

            if (jsonObject.has(RequestFields.REQUEST.toString())) {
                return JSONSerializable.deserialize(Request.class, jsonObject);
            }
            else if (jsonObject.has(ResponseFields.RESPONSE.toString())) {
                return JSONSerializable.deserialize(Response.class, jsonObject);
            }
        } while (true);
    }

    /**
     * Waits for the client to send a {@link Request}.
     *
     * @return the {@link Request} created by the client
     * @throws IOException if any IO error occurs
     */
    protected Request waitForRequest() throws IOException {
        return JSONSerializable.deserialize(Request.class, this.in.readJSON());
    }

    /**
     * Sends a {@link Response} to the client.
     *
     * @param response sends a {@link Response} to the connected client
     * @throws IOException if any IO error occurs
     */
    public void sendResponse(Response response) throws IOException {
        this.out.writeJSON(response.serialize());
    }
    
    /**
     * Sends a {@link Request} to the client.
     *
     * @param request sends a {@link Request} to the connected client
     * @throws IOException if any IO error occurs
     */
    public void sendRequest(Request request) throws IOException {
        this.out.writeJSON(request.serialize());
    }

    public Socket getClient() {
        return client;
    }

    protected Middleware handleIncomingData() throws Exception {
        SocketAddress socketAddress = this.client.getRemoteSocketAddress();

        JSONSerializable data = waitForData();

        ServerLogger.getLogger()
                .fine(() -> String.format(
                        "Got data \"%s\", from client: %s",
                        data.toString(),
                        socketAddress.toString()
                ));

        if (data instanceof Request) {
            return this.handleIncomingRequest((Request<? extends JSONSerializable>) data);
        }
        else if (data instanceof Response) {
            return this.handleIncomingResponse((Response<? extends JSONSerializable>) data);
        }

        return null;
    }

    protected Middleware handleIncomingRequest(Request<? extends JSONSerializable> request) throws Exception {
        EndPointFunction endPointFunction = request.getHeader().getEndPointFunction();

        MiddlewareFactory<?> middlewareFactory = endPointFunctionMiddlewareFactoryEnumMap.getOrDefault(endPointFunction, null);

        Middleware middleware;

        if (middlewareFactory == null) {
            middleware = this.dynamicMiddlewares.getOrDefault(endPointFunction, null);
        }
        else {
            middleware = middlewareFactory.instantiateMiddleware();
        }

        if (middleware == null) {
            return null;
        }

        middleware.prepare();

        Response response = middleware.handleRequest(request, endPointFunction, this);

        if (response == null) {
            return null;
        }

        if (response.getBody() instanceof NullResponse) {
            return middleware;
        }

        this.sendResponse(response);

        return middleware;
    }

    protected Middleware handleIncomingResponse(Response<? extends JSONSerializable> response) throws Exception {
        EndPointFunction endPointFunction = response.getHeader().getEndPointFunction();

        MiddlewareFactory<?> middlewareFactory = endPointFunctionMiddlewareFactoryEnumMap.getOrDefault(endPointFunction, null);

        if (middlewareFactory == null) {
            return null;
        }

        Middleware middleware = middlewareFactory.instantiateMiddleware();
        middleware.prepare();

        Request request = middleware.handleResponse(response, endPointFunction, this);

        if (request == null) {
            return null;
        }

        if (request.getBody() instanceof NullResponse) {
            return middleware;
        }

        this.sendRequest(request);

        return middleware;
    }
}
