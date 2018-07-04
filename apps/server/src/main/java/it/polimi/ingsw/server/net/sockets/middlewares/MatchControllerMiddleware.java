package it.polimi.ingsw.server.net.sockets.middlewares;

import it.polimi.ingsw.controllers.NotEnoughTokensException;
import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.core.ToolCardConditionException;
import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.ResponseError;
import it.polimi.ingsw.net.requests.*;
import it.polimi.ingsw.net.responses.*;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.net.utils.ResponseFields;
import it.polimi.ingsw.server.managers.MatchCommunicationsManager;
import it.polimi.ingsw.server.net.sockets.AuthenticatedClientHandler;
import it.polimi.ingsw.server.net.sockets.ClientHandler;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

@Handles({
        // ------ RESPONSES ------
        EndPointFunction.MATCH_WINDOW_RESPONSE,
        EndPointFunction.MATCH_PLAYER_TOOL_CARD_CHOOSE_POSITION_RESPONSE,
        EndPointFunction.MATCH_PLAYER_TOOL_CARD_CHOOSE_BETWEEN_ACTIONS_RESPONSE,
        EndPointFunction.MATCH_PLAYER_TOOL_CARD_CHOOSE_SHADE_RESPONSE,
        EndPointFunction.MATCH_PLAYER_TOOL_CARD_SHOULD_CONTINUE_TO_REPEAT_RESPONSE,
        
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

            MoveResponse moveResponse = this.matchCommunicationsManager.onMoveRequested(
                    ((AuthenticatedClientHandler) clientHandler).getPlayer(),
                    Move.of(moveRequest.getBody().getFrom(), moveRequest.getBody().getTo())
            );

            return new Response<>(
                    new Header(
                            EndPointFunction.MATCH_PLAYER_MOVE_REQUEST
                    ),
                    moveResponse
            );
        }
        else if (endPointFunction == EndPointFunction.MATCH_PLAYER_TURN_END_REQUEST && request.getBody() instanceof MatchEndRequest) {
            this.matchCommunicationsManager.onEndRequested(
                    ((AuthenticatedClientHandler) clientHandler).getPlayer()
            );

            return new Response<>(
                    request.getHeader(),
                    new NullResponse()
            );
        }
        else if (endPointFunction == EndPointFunction.MATCH_PLAYER_TOOL_CARD_REQUEST && request.getBody() instanceof ToolCardUsageRequest) {
            Request<ToolCardUsageRequest> toolCardUsageRequest = (Request<ToolCardUsageRequest>) request;

            try {
                this.matchCommunicationsManager.onToolCardRequested(
                        ((AuthenticatedClientHandler) clientHandler).getPlayer(),
                        toolCardUsageRequest.getBody().getToolCard()
                );
            }
            catch (ToolCardConditionException e) {
                return new Response<>(
                        new Header(endPointFunction),
                        new ResponseError(
                                ResponseFields.Error.CONSTRAINT_EVALUATION
                        )
                );
            }
            catch (NotEnoughTokensException e) {
                return new Response<>(
                        new Header(endPointFunction),
                        new ResponseError(
                                ResponseFields.Error.NOT_ENOUGH_TOKENS
                        )
                );
            }
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

            return new Request(
                    response.getHeader(),
                    new NullRequest()
            );
        }
        else if (endPointFunction == EndPointFunction.MATCH_PLAYER_TOOL_CARD_CHOOSE_POSITION_RESPONSE && response.getBody() instanceof ChoosePositionForLocationResponse) {
            Response<ChoosePositionForLocationResponse> r = (Response<ChoosePositionForLocationResponse>) response;

            this.matchCommunicationsManager.onPositionChosen(
                    r.getBody().getLocation()
            );

            return new Request(
                    response.getHeader(),
                    new NullRequest()
            );
        }
        else if (endPointFunction == EndPointFunction.MATCH_PLAYER_TOOL_CARD_CHOOSE_BETWEEN_ACTIONS_RESPONSE && response.getBody() instanceof ChooseBetweenActionsResponse) {
            Response<ChooseBetweenActionsResponse> r = (Response<ChooseBetweenActionsResponse>) response;

            this.matchCommunicationsManager.onActionsChosen(
                    r.getBody().getChosenActions()
            );

            return new Request(
                    response.getHeader(),
                    new NullRequest()
            );
        }
        else if (endPointFunction == EndPointFunction.MATCH_PLAYER_TOOL_CARD_CHOOSE_SHADE_RESPONSE && response.getBody() instanceof SetShadeResponse) {
            Response<SetShadeResponse> r = (Response<SetShadeResponse>) response;

            this.matchCommunicationsManager.onShadeChosen(
                    r.getBody().getChosenShade()
            );

            return new Request(
                    response.getHeader(),
                    new NullRequest()
            );
        }
        else if (endPointFunction == EndPointFunction.MATCH_PLAYER_TOOL_CARD_SHOULD_CONTINUE_TO_REPEAT_RESPONSE && response.getBody() instanceof ShouldRepeatResponse) {
            Response<ShouldRepeatResponse> r = (Response<ShouldRepeatResponse>) response;

            this.matchCommunicationsManager.onShouldRepeatResponse(
                    r.getBody().shouldRepeat()
            );

            return new Request(
                    response.getHeader(),
                    new NullRequest()
            );
        }

        return null;
    }

    @Override
    public boolean shouldBeKeptAlive() {
        return true;
    }
}
