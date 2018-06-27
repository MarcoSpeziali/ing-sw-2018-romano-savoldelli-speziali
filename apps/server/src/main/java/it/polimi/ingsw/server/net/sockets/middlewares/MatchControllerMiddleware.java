package it.polimi.ingsw.server.net.sockets.middlewares;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.responses.WindowResponse;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.managers.MatchCommunicationsManager;
import it.polimi.ingsw.server.net.sockets.AuthenticatedClientHandler;
import it.polimi.ingsw.server.net.sockets.ClientHandler;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

@Handles(EndPointFunction.MATCH_WINDOW_RESPONSE)
public class MatchControllerMiddleware implements Middleware {

    private final MatchCommunicationsManager matchCommunicationsManager;

    public MatchControllerMiddleware(MatchCommunicationsManager matchCommunicationsManager) {
        this.matchCommunicationsManager = matchCommunicationsManager;
    }

    @Override
    public Response handleRequest(Request<? extends JSONSerializable> request, EndPointFunction endPointFunction, ClientHandler clientHandler) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Request handleResponse(Response<? extends JSONSerializable> response, EndPointFunction endPointFunction, ClientHandler clientHandler) {
        if (response.getBody() instanceof WindowResponse) {
            Response<WindowResponse> windowResponse = (Response<WindowResponse>) response;
            this.matchCommunicationsManager.onWindowChosen(
                    ((AuthenticatedClientHandler) clientHandler).getPlayer(),
                    windowResponse.getBody().getChosenWindow()
            );
        }

        return null;
    }

    @Override
    public boolean shouldBeKeptAlive() {
        return true;
    }
}
