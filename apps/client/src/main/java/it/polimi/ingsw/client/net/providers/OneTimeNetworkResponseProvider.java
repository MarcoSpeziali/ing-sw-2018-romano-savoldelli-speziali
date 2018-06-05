package it.polimi.ingsw.client.net.providers;

import it.polimi.ingsw.net.Body;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.function.Consumer;

public interface OneTimeNetworkResponseProvider {

    /**
     * Sends a {@link Request} to the server and waits for a {@link Response} (blocking call).
     * Then the connection is terminated.
     *
     * @param request the {@link Request} to send
     * @param hostAddress the address of the host
     * @param hostPort the port of the host
     * @return a {@link Response} produced by the server
     * @throws IOException if any IO error occurs
     * @throws NotBoundException if {@link Body#getEndPointFunction()} is not currently bound
     * @throws ReflectiveOperationException if a reflection error occurs
     */
    Response getSyncResponseFor(Request request, String hostAddress, int hostPort) throws IOException, NotBoundException, ReflectiveOperationException;

    /**
     * Sends a {@link Request} to the server.
     * The connection is opened using the default address and port (from {@link it.polimi.ingsw.client.Settings}).
     * Then the connection is terminated.
     *
     * @param request the {@link Request} to send
     * @return a {@link Response} produced by the server
     * @throws IOException if any IO error occurs
     * @throws NotBoundException if {@link Body#getEndPointFunction()} is not currently bound
     * @throws ReflectiveOperationException if a reflection error occurs
     */
    Response getSyncResponseFor(Request request) throws IOException, NotBoundException, ReflectiveOperationException;

    /**
     * Sends a {@link Request} to the server.
     * Then the connection is terminated.
     *
     * @param request the {@link Request} to send
     * @param hostAddress the address of the host
     * @param hostPort the port of the host
     * @param responseConsumer the {@link Consumer} which holds the {@link Response} produced by the server
     * @param onError the {@link Consumer} called when an {@link Exception} occurs
     */
    default void getAsyncResponseFor(Request request, String hostAddress, int hostPort, Consumer<Response> responseConsumer, Consumer<Exception> onError) {
        new Thread(() -> {
            try {
                responseConsumer.accept(
                        getSyncResponseFor(request, hostAddress, hostPort)
                );
            }
            catch (IOException | ReflectiveOperationException | NotBoundException e) {
                if (onError != null) {
                    onError.accept(e);
                }
            }
        }).start();
    }

    /**
     * Sends a {@link Request} to the server for a {@link Response} (blocking call).
     * The connection is opened using the default address and port (from {@link it.polimi.ingsw.client.Settings}).
     * Then the connection is terminated.
     *
     * @param request the {@link Request} to send
     * @param responseConsumer the {@link Consumer} which holds the {@link Response} produced by the server
     * @param onError the {@link Consumer} called when an {@link Exception} occurs
     */
    default void getAsyncResponseFor(Request request, Consumer<Response> responseConsumer, Consumer<Exception> onError) {
        new Thread(() -> {
            try {
                responseConsumer.accept(
                        getSyncResponseFor(request)
                );
            }
            catch (IOException | ReflectiveOperationException | NotBoundException e) {
                if (onError != null) {
                    onError.accept(e);
                }
            }
        }).start();
    }
}
