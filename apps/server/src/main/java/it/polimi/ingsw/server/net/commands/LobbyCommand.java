package it.polimi.ingsw.server.net.commands;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.mocks.ILobby;
import it.polimi.ingsw.net.requests.LobbyJoinRequest;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.net.endpoints.LobbyEndPoint;

import java.io.IOException;
import java.net.Socket;

@Handles(value = EndPointFunction.LOBBY_JOIN_REQUEST, requiresAuthentication = true)
public class LobbyCommand implements Command<ILobby, LobbyJoinRequest> {

    @Override
    public Response<ILobby> handle(Request<LobbyJoinRequest> request, Socket client) throws IOException {
        return LobbyEndPoint.getInstance().joinLobby(request);
    }

    @Override
    public boolean shouldBeKeptAlive() {
        return true;
    }
}
