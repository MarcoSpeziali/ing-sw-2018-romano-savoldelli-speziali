package it.polimi.ingsw.server.net.sockets;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

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
}
