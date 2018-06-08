package it.polimi.ingsw.net.providers;

import it.polimi.ingsw.net.Body;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.utils.RequestFields;
import it.polimi.ingsw.net.utils.ResponseFields;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class PersistentSocketInteractionProvider extends PersistentNetworkInteractionProvider {

    private boolean shouldStop = false;

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    protected PersistentSocketInteractionProvider(String remoteAddress, int remotePort) {
        super(remoteAddress, remotePort);
    }

    public void open() throws IOException {
        this.socket = new Socket(remoteAddress, remotePort);
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
    public Response getSyncResponseFor(Request request) throws IOException, NotBoundException, ReflectiveOperationException {
        if (socket == null) {
            throw new IllegalStateException("The connection hasn't been opened yet, or it has been closed. Call the method open() to open the connection.");
        }

        AtomicReference<Response> receivedResponse = new AtomicReference<>();

        Consumer<Response> oldListener = this.responseListeners.put(
                request.getHeader().getEndPointFunction(),
                receivedResponse::set
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
                    request.getHeader().getEndPointFunction(), oldListener
            );
        }

        return receivedResponse.get();
    }

    @Override
    public void getAsyncResponseFor(Request request, Consumer<Response> responseConsumer, Consumer<Exception> onError) {
        if (socket == null) {
            throw new IllegalStateException("The connection hasn't been opened yet, or it has been closed. Call the method open() to open the connection.");
        }

        try {
            Consumer<Response> oldListener = this.responseListeners.getOrDefault(
                    request.getHeader().getEndPointFunction(),
                    null
            );

            this.responseListeners.put(
                    request.getHeader().getEndPointFunction(),
                    response -> {
                        responseConsumer.accept(response);

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

    private Runnable inputListenerRunnable = () -> {
        while (!shouldStop) {
            try {
                String content = this.in.readLine();

                JSONObject jsonObject = new JSONObject(content);

                if (jsonObject.has(ResponseFields.RESPONSE.toString())) {
                    Response response = new Response();
                    response.deserialize(new JSONObject(content));

                    this.handleResponse(response);
                }
                else if (jsonObject.has(RequestFields.REQUEST.toString())) {
                    Request request = new Request();
                    request.deserialize(new JSONObject(content));

                    Response response = handleRequest(request);

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

    private void handleResponse(Response response) {
        if (response.getError() != null && this.errorListeners.containsKey(response.getHeader().getEndPointFunction())) {
            this.errorListeners.get(response.getHeader().getEndPointFunction()).accept(response.getError());
            return;
        }

        if (this.responseListeners.containsKey(response.getHeader().getEndPointFunction())) {
            this.responseListeners.get(response.getHeader().getEndPointFunction()).accept(response);
        }
    }

    private Response handleRequest(Request request) {
        if (this.requestListeners.containsKey(request.getHeader().getEndPointFunction())) {
            return this.requestListeners.get(request.getHeader().getEndPointFunction()).apply(request);
        }

        return null;
    }
}
