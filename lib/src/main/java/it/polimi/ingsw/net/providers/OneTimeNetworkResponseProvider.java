package it.polimi.ingsw.net.providers;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.function.Consumer;

/**
 * Represents a non-persistent network {@link Response} provider, which, after receiving the
 * {@link Response}, closes the connection.
 */
public interface OneTimeNetworkResponseProvider {

    // TODO: docs
    String getServerAddress();

    // TODO: docs
    int getServerPort();

    /**
     * Sends a {@link Request} to the server and waits for a {@link Response} (blocking call).
     * Then the connection is terminated.
     *
     * @param request     the {@link Request} to send
     * @param hostAddress the address of the host
     * @param hostPort    the port of the host
     * @return a {@link Response} produced by the server
     * @throws IOException       if any IO error occurs
     * @throws NotBoundException if the endpoint is not currently bound
     */
    <T extends JSONSerializable, K extends JSONSerializable> Response<T> getSyncResponseFor(Request<K> request, String hostAddress, int hostPort) throws IOException, NotBoundException;

    /**
     * Sends a {@link Request} to the server.
     * Then the connection is terminated.
     *
     * @param request          the {@link Request} to send
     * @param hostAddress      the address of the host
     * @param hostPort         the port of the host
     * @param responseConsumer the {@link Consumer} which holds the {@link Response} produced by the server
     * @param onError          the {@link Consumer} called when an {@link Exception} occurs
     */
    default <T extends JSONSerializable, K extends JSONSerializable> void getAsyncResponseFor(Request<K> request, String hostAddress, int hostPort, Consumer<Response<T>> responseConsumer, Consumer<Exception> onError) {
        new Thread(() -> {
            try {
                responseConsumer.accept(
                        getSyncResponseFor(request, hostAddress, hostPort)
                );
            }
            catch (IOException | NotBoundException e) {
                if (onError != null) {
                    onError.accept(e);
                }
            }
        }).start();
    }

    default <T extends JSONSerializable, K extends JSONSerializable> void getAsyncResponseFor(Request<K> request, Consumer<Response<T>> responseConsumer, Consumer<Exception> onError) {
        new Thread(() -> {
            try {
                responseConsumer.accept(
                        getSyncResponseFor(request, getServerAddress(), getServerPort())
                );
            }
            catch (IOException | NotBoundException e) {
                if (onError != null) {
                    onError.accept(e);
                }
            }
        }).start();
    }

    default <T extends JSONSerializable, K extends JSONSerializable> Response<T> getSyncResponseFor(Request<K> request) throws IOException, NotBoundException {
        return getSyncResponseFor(
                request,
                getServerAddress(),
                getServerPort()
        );
    }
}
