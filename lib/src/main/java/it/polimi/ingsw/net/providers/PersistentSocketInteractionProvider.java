package it.polimi.ingsw.net.providers;

import it.polimi.ingsw.net.Body;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.ResponseError;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.net.utils.RequestFields;
import it.polimi.ingsw.net.utils.ResponseFields;
import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

// TODO: docs
public class PersistentSocketInteractionProvider extends PersistentNetworkInteractionProvider {

    private boolean shouldStop = false;

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    /**
     * A {@link Map} of {@link Response}s listeners.
     */
    private Map<EndPointFunction, Consumer<Response<? extends JSONSerializable>>> responseListeners = new EnumMap<>(EndPointFunction.class);

    /**
     * A {@link Map} of {@link Request}s listeners.
     */
    private Map<EndPointFunction, Function<Request<? extends JSONSerializable>, Response<? extends JSONSerializable>>> requestListeners = new EnumMap<>(EndPointFunction.class);

    /**
     * A {@link Map} of {@link ResponseError}s listeners.
     */
    private Map<EndPointFunction, Consumer<ResponseError>> errorListeners = new EnumMap<>(EndPointFunction.class);
    private Runnable inputListenerRunnable = () -> {
        while (!shouldStop) {
            try {
                String content = this.in.readLine();

                JSONObject jsonObject = new JSONObject(content);

                if (jsonObject.has(ResponseFields.RESPONSE.toString())) {
                    Response<? extends JSONSerializable> response = new Response<>();
                    response.deserialize(new JSONObject(content));

                    this.handleResponse(response);
                }
                else if (jsonObject.has(RequestFields.REQUEST.toString())) {
                    Request<? extends JSONSerializable> request = new Request<>();
                    request.deserialize(new JSONObject(content));

                    Response<? extends JSONSerializable> response = handleRequest(request);

                    if (response != null) {
                        this.out.write(response.toString());
                        this.out.newLine();
                        this.out.flush();
                    }
                }
            }
            catch (IOException ignored) {
            }
        }
    };

    protected PersistentSocketInteractionProvider(String remoteAddress, int remotePort) {
        super(remoteAddress, remotePort);
    }

    /**
     * Adds a listener for the target endpoint.
     *
     * @param endPointTarget   the target endpoint
     * @param responseConsumer the {@link Consumer} that consumes the received {@link Response}
     */
    public void listenFor(EndPointFunction endPointTarget, Consumer<Response<? extends JSONSerializable>> responseConsumer) {
        this.responseListeners.put(endPointTarget, responseConsumer);
    }

    /**
     * Adds a listener for the target endpoint.
     *
     * @param endPointTarget   the target endpoint
     * @param responseFunction the {@link Function} that consumes the received
     *                         {@link Request} and produces a {@link Response}
     */
    public void listenFor(EndPointFunction endPointTarget, Function<Request<? extends JSONSerializable>, Response<? extends JSONSerializable>> responseFunction) {
        this.requestListeners.put(endPointTarget, responseFunction);
    }

    public void listerFor(EndPointFunction endPointTarget, Consumer<ResponseError> errorConsumer) {
        this.errorListeners.put(endPointTarget, errorConsumer);
    }

    public void open() throws IOException {
        this.socket = new Socket();
        this.socket.connect(InetSocketAddress.createUnresolved(this.remoteAddress, this.remotePort), 1000);

        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));

        Thread inputThread = new Thread(this.inputListenerRunnable);
        inputThread.start();
    }

    /**
     * Sends a {@link Request} to the server and waits for a {@link Response} (blocking call).
     *
     * @param request the {@link Request} to send
     * @return a {@link Response} produced by the server
     * @throws IOException                  if any IO error occurs
     * @throws NotBoundException            if {@link Body#getEndPointFunction()} is not currently bound
     * @throws ReflectiveOperationException if a reflection error occurs
     */
    @Override
    public <T extends JSONSerializable, K extends JSONSerializable> Response<T> getSyncResponseFor(Request<K> request) throws IOException, NotBoundException, ReflectiveOperationException {
        if (socket == null) {
            throw new IllegalStateException("The connection hasn't been opened yet, or it has been closed. Call the method open() to open the connection.");
        }

        AtomicReference<Consumer<Response<T>>> receivedResponse = new AtomicReference<>();

        Consumer<Response<? extends JSONSerializable>> oldListener = this.responseListeners.getOrDefault(
                request.getHeader().getEndPointFunction(),
                null
        );

        this.responseListeners.put(
                request.getHeader().getEndPointFunction(),
                newValue -> receivedResponse.set((Consumer<Response<T>>) newValue)
        );

        this.out.write(request.toString());
        this.out.newLine();
        this.out.flush();

        //noinspection StatementWithEmptyBody
        while (receivedResponse.get() == null) {
            // waits for a response
        }

        if (oldListener == null) {
            this.responseListeners.remove(request.getHeader().getEndPointFunction());
        }
        else {
            this.responseListeners.put(
                    request.getHeader().getEndPointFunction(),
                    oldListener
            );
        }

        return (Response<T>) receivedResponse.get();
    }

    @Override
    public <T extends JSONSerializable, K extends JSONSerializable> void getAsyncResponseFor(Request<K> request, Consumer<Response<T>> responseConsumer, Consumer<Exception> onError) {
        if (socket == null) {
            throw new IllegalStateException("The connection hasn't been opened yet, or it has been closed. Call the method open() to open the connection.");
        }

        try {
            Consumer<Response<? extends JSONSerializable>> oldListener = this.responseListeners.getOrDefault(
                    request.getHeader().getEndPointFunction(),
                    null
            );

            this.responseListeners.put(
                    request.getHeader().getEndPointFunction(),
                    response -> {
                        responseConsumer.accept((Response<T>) response);

                        if (oldListener == null) {
                            this.responseListeners.remove(request.getHeader().getEndPointFunction());
                        }
                        else {
                            this.responseListeners.put(
                                    request.getHeader().getEndPointFunction(), oldListener
                            );
                        }
                    }
            );

            this.out.write(request.toString());
            this.out.newLine();
            this.out.flush();
        }
        catch (IOException e) {
            if (onError != null) {
                onError.accept(e);
            }
        }
    }

    @Override
    public void close() throws IOException {
        this.shouldStop = true;

        this.requestListeners.clear();
        this.responseListeners.clear();

        this.socket.close();
    }

    private <T extends JSONSerializable> void handleResponse(Response<T> response) {
        if (response.getError() != null && this.errorListeners.containsKey(response.getHeader().getEndPointFunction())) {
            this.errorListeners.get(response.getHeader().getEndPointFunction()).accept(response.getError());
        }
        else if (this.responseListeners.containsKey(response.getHeader().getEndPointFunction())) {
            this.responseListeners.get(response.getHeader().getEndPointFunction()).accept(response);
        }
    }

    private <T extends JSONSerializable, K extends JSONSerializable> Response<T> handleRequest(Request<K> request) {
        if (this.requestListeners.containsKey(request.getHeader().getEndPointFunction())) {
            return (Response<T>) this.requestListeners.get(request.getHeader().getEndPointFunction()).apply(request);
        }

        return null;
    }
}
