package it.polimi.ingsw.server.net.sockets.middlewares;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.requests.LobbyJoinRequest;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.net.endpoints.LobbyEndPoint;
import it.polimi.ingsw.server.net.sockets.ClientHandler;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

import java.rmi.RemoteException;

@Handles(EndPointFunction.LOBBY_JOIN_REQUEST)
public class LobbyMiddleware implements Middleware {

    private LobbyEndPoint lobbyEndPoint;

    @Override
    public void prepare() throws RemoteException {
        this.lobbyEndPoint = LobbyEndPoint.getInstance();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Response handleRequest(Request<? extends JSONSerializable> request, EndPointFunction endPointFunction, ClientHandler clientHandler) {
        if (request.getBody() instanceof LobbyJoinRequest) {
            return this.lobbyEndPoint.joinLobby((Request<LobbyJoinRequest>) request);
        }

        return null;
    }

    @Override
    public Request handleResponse(Response<? extends JSONSerializable> response, EndPointFunction endPointFunction, ClientHandler clientHandler) {
        return null;
    }

    @Override
    public boolean shouldBeKeptAlive() {
        return true;
    }
}
