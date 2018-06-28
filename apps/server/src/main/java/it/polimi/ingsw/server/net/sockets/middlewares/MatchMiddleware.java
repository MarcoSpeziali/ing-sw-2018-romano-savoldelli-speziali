package it.polimi.ingsw.server.net.sockets.middlewares;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.net.requests.NullRequest;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.net.endpoints.MatchEndPoint;
import it.polimi.ingsw.server.net.sockets.ClientHandler;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

import java.rmi.RemoteException;

@Handles(EndPointFunction.MATCH_MIGRATION)
public class MatchMiddleware implements Middleware {

    @Override
    public Response handleRequest(Request<? extends JSONSerializable> request, EndPointFunction endPointFunction, ClientHandler clientHandler) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Request handleResponse(Response<? extends JSONSerializable> response, EndPointFunction endPointFunction, ClientHandler clientHandler) {
        if (response.getBody() instanceof MatchInteraction) {
            Response<MatchInteraction> matchInteractionResponse = (Response<MatchInteraction>) response;

            try {
                MatchEndPoint matchEndPoint = MatchEndPoint.getEndPointForMatch(matchInteractionResponse.getBody().getMatchId());
                matchEndPoint.confirmMatchJoin(matchInteractionResponse);

                return new Request<>(
                        response.getHeader(),
                        new NullRequest()
                );
            }
            catch (RemoteException e) {
                return null;
            }
        }

        return null;
    }

    @Override
    public boolean shouldBeKeptAlive() {
        return true;
    }
}
