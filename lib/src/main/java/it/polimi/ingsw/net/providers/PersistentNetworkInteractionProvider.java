package it.polimi.ingsw.net.providers;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.function.Consumer;

/**
 * Represents a persistent network {@link Response} provider, which, after receiving the
 * {@link Response}, keeps the connection open.
 */
public abstract class PersistentNetworkInteractionProvider implements AutoCloseable {

    /**
     * The remote host address.
     */
    protected String remoteAddress;

    /**
     * The remote host port.
     */
    protected int remotePort;

    protected PersistentNetworkInteractionProvider(String remoteAddress, int remotePort) {
        this.remoteAddress = remoteAddress;
        this.remotePort = remotePort;
    }

    /**
     * Opens the connection.
     *
     * @param remoteEndPoint the endpoint to connect to
     * @throws IOException if any IO error occurs
     */
    public abstract void open(EndPointFunction remoteEndPoint) throws IOException, NotBoundException;

    /**
     * Sends a {@link Request} to the server and waits for a {@link Response} (blocking call).
     *
     * @param request the {@link Request} to send
     * @return a {@link Response} produced by the server
     * @throws IOException                  if any IO error occurs
     * @throws NotBoundException            if the endpoint is not currently bound
     * @throws ReflectiveOperationException if a reflection error occurs
     */
    public abstract <T extends JSONSerializable, K extends JSONSerializable> Response<T> getSyncResponseFor(Request<K> request) throws IOException, NotBoundException, ReflectiveOperationException;

    /**
     * Sends a {@link Request} to the server.
     *
     * @param request          the {@link Request} to send
     * @param responseConsumer the {@link Consumer} which holds the {@link Response} produced by the server
     * @param onError          the {@link Consumer} called when an {@link Exception} occurs
     */
    public <T extends JSONSerializable, K extends JSONSerializable> void getAsyncResponseFor(Request<K> request, Consumer<Response<T>> responseConsumer, Consumer<Exception> onError) {
        new Thread(() -> {
            try {
                responseConsumer.accept(
                        this.getSyncResponseFor(request)
                );
            }
            catch (IOException | ReflectiveOperationException | NotBoundException e) {
                if (onError != null) {
                    onError.accept(e);
                }
            }
        }).start();
    }

    @Override
    public abstract void close() throws IOException;
}
