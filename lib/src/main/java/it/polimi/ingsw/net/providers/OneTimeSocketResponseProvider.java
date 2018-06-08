package it.polimi.ingsw.net.providers;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.utils.io.JSONSerializable;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class OneTimeSocketResponseProvider implements OneTimeNetworkResponseProvider {

    /**
     * Sends a {@link Request} to the server and waits for a {@link Response} (blocking call).
     * Then the connection is terminated.
     *
     * @param request     the {@link Request} to send
     * @param hostAddress the address of the host
     * @param hostPort    the port of the host
     * @return a {@link Response} produced by the server
     * @throws IOException if any IO error occurs
     */
    @Override
    public <T extends JSONSerializable, K extends JSONSerializable> Response<T> getSyncResponseFor(Request<K> request, String hostAddress, int hostPort) throws IOException {
        try (Socket socket = new Socket(hostAddress, hostPort)) {
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            output.write(request.serialize().toString());
            output.newLine();
            output.flush();

            String content = input.readLine();

            Response<T> response = new Response<>();
            response.deserialize(new JSONObject(content));

            return response;
        }
    }
}
