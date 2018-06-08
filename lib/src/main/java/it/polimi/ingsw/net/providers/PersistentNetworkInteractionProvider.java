package it.polimi.ingsw.net.providers;

import it.polimi.ingsw.net.Body;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.ResponseError;
import it.polimi.ingsw.net.utils.EndPointFunction;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents a persistent network {@link Response} provider, which, after receiving the
 * {@link Response}, keeps the connection open.
 */
public abstract class PersistentNetworkInteractionProvider implements AutoCloseable {

    private static final long serialVersionUID = 1680783992994102775L;

    /**
     * The remote host address.
     */
    protected String remoteAddress;

    /**
     * The remote host port.
     */
    protected int remotePort;

    /**
     * A {@link Map} of {@link Response}s listeners.
     */
    protected Map<EndPointFunction, Consumer<Response>> responseListeners = new EnumMap<>(EndPointFunction.class);

    /**
     * A {@link Map} of {@link Request}s listeners.
     */
    protected Map<EndPointFunction, Function<Request, Response>> requestListeners = new EnumMap<>(EndPointFunction.class);

    /**
     * A {@link Map} of {@link ResponseError}s listeners.
     */
    protected Map<EndPointFunction, Consumer<ResponseError>> errorListeners = new EnumMap<>(EndPointFunction.class);

    protected PersistentNetworkInteractionProvider(String remoteAddress, int remotePort) {
        this.remoteAddress = remoteAddress;
        this.remotePort = remotePort;
    }

    /**
     * Adds a listener for the target endpoint.
     * @param endPointTarget the target endpoint
     * @param responseConsumer the {@link Consumer} that consumes the received {@link Response}
     */
    public void listenFor(EndPointFunction endPointTarget, Consumer<Response> responseConsumer) {
        this.responseListeners.put(endPointTarget, responseConsumer);
    }

    /**
     * Adds a listener for the target endpoint.
     * @param endPointTarget the target endpoint
     * @param responseFunction the {@link Function} that consumes the received
     *                         {@link Request} and produces a {@link Response}
     */
    public void listenFor(EndPointFunction endPointTarget, Function<Request, Response> responseFunction) {
        this.requestListeners.put(endPointTarget, responseFunction);
    }

    /**
     * Sends a {@link Request} to the server and waits for a {@link Response} (blocking call).
     *
     * @param request the {@link Request} to send
     * @return a {@link Response} produced by the server
     * @throws IOException if any IO error occurs
     * @throws NotBoundException if {@link Body#getEndPointFunction()} is not currently bound
     * @throws ReflectiveOperationException if a reflection error occurs
     */
    public abstract Response getSyncResponseFor(Request request) throws IOException, NotBoundException, ReflectiveOperationException;

    /**
     * Sends a {@link Request} to the server.
     *
     * @param request the {@link Request} to send
     * @param responseConsumer the {@link Consumer} which holds the {@link Response} produced by the server
     * @param onError the {@link Consumer} called when an {@link Exception} occurs
     */
    public void getAsyncResponseFor(Request request, Consumer<Response> responseConsumer, Consumer<Exception> onError) {
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
