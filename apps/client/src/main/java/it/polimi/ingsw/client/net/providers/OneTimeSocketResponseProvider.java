package it.polimi.ingsw.client.net.providers;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.Settings;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
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
    public Response getSyncResponseFor(Request request, String hostAddress, int hostPort) throws IOException {
        try (Socket socket = new Socket(hostAddress, hostPort)) {
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            output.write(request.serialize().toString());
            output.newLine();
            output.flush();

            String content = input.readLine();

            Response response = new Response();
            response.deserialize(new JSONObject(content));

            return response;
        }
    }

    /**
     * Sends a {@link Request} to the server.
     * The connection is opened using the default address and port (took from {@link Constants}).
     * Then the connection is terminated.
     *
     * @param request the {@link Request} to send
     * @return a {@link Response} produced by the server
     * @throws IOException if any IO error occurs
     */
    @Override
    public Response getSyncResponseFor(Request request) throws IOException {
        return getSyncResponseFor(
                request,
                Settings.getSettings().getServerSocketAddress(),
                Settings.getSettings().getServerSocketPort()
        );
    }
}
