package it.polimi.ingsw.server.net.sockets.middlewares;

import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.requests.MatchEndRequest;
import it.polimi.ingsw.net.requests.MoveRequest;
import it.polimi.ingsw.net.requests.ToolCardUsageRequest;
import it.polimi.ingsw.net.responses.WindowResponse;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.managers.MatchCommunicationsManager;
import it.polimi.ingsw.server.net.sockets.AuthenticatedClientHandler;
import it.polimi.ingsw.server.net.sockets.ClientHandler;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

@Handles({
        // ------ RESPONSES ------
        EndPointFunction.MATCH_WINDOW_RESPONSE,
        
        // ------ REQUESTS ------
        EndPointFunction.MATCH_PLAYER_MOVE_REQUEST,
        EndPointFunction.MATCH_PLAYER_TURN_END_REQUEST,
        EndPointFunction.MATCH_PLAYER_TOOL_CARD_REQUEST
})
public class MatchControllerMiddleware implements Middleware {

    private final MatchCommunicationsManager matchCommunicationsManager;

    public MatchControllerMiddleware(MatchCommunicationsManager matchCommunicationsManager) {
        this.matchCommunicationsManager = matchCommunicationsManager;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Response handleRequest(Request<? extends JSONSerializable> request, EndPointFunction endPointFunction, ClientHandler clientHandler) {
        if (endPointFunction == EndPointFunction.MATCH_PLAYER_MOVE_REQUEST && request.getBody() instanceof MoveRequest) {
            Request<MoveRequest> moveRequest = (Request<MoveRequest>) request;
            
            this.matchCommunicationsManager.onMoveRequested(
                    ((AuthenticatedClientHandler) clientHandler).getPlayer(),
                    Move.of(moveRequest.getBody().getFrom(), moveRequest.getBody().getTo())
            );
        }
        else if (endPointFunction == EndPointFunction.MATCH_PLAYER_TURN_END_REQUEST && request.getBody() instanceof MatchEndRequest) {
            this.matchCommunicationsManager.onEndRequested(
                    ((AuthenticatedClientHandler) clientHandler).getPlayer()
            );
        }
        else if (endPointFunction == EndPointFunction.MATCH_PLAYER_TOOL_CARD_REQUEST && request.getBody() instanceof ToolCardUsageRequest) {
            Request<ToolCardUsageRequest> toolCardUsageRequest = (Request<ToolCardUsageRequest>) request;
            
            this.matchCommunicationsManager.onToolCardRequested(
                    ((AuthenticatedClientHandler) clientHandler).getPlayer(),
                    toolCardUsageRequest.getBody().getToolCard()
            );
        }
        
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Request handleResponse(Response<? extends JSONSerializable> response, EndPointFunction endPointFunction, ClientHandler clientHandler) {
        if (endPointFunction == EndPointFunction.MATCH_WINDOW_RESPONSE && response.getBody() instanceof WindowResponse) {
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
