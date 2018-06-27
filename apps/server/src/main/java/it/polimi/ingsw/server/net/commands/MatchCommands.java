package it.polimi.ingsw.server.net.commands;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.net.responses.WindowResponse;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.net.endpoints.MatchEndPoint;

import java.io.IOException;
import java.net.Socket;

public final class MatchCommands {

    private MatchCommands() {}
    
    @Handles(value = EndPointFunction.MATCH_MIGRATION, requiresAuthentication = true)
    public static final class MigrationCommand implements InvertedCommand.NullRequest<MatchInteraction> {
    
        @Override
        public Request<it.polimi.ingsw.net.requests.NullRequest> handle(Response<MatchInteraction> matchInteractionResponse, Socket client) throws IOException {
            MatchEndPoint matchEndPoint = MatchEndPoint.getEndPointForMatch(matchInteractionResponse.getBody().getMatchId());
            matchEndPoint.confirmMatchJoin(matchInteractionResponse);
            
            return nullRequest(matchInteractionResponse);
        }
    
        @Override
        public boolean shouldBeKeptAlive() {
            return true;
        }
    }
    
    @Handles(value = EndPointFunction.MATCH_WINDOW_RESPONSE, requiresAuthentication = true)
    public static final class WindowResponseCommand implements InvertedCommand.NullRequest<WindowResponse> {
    
        @Override
        public Request<it.polimi.ingsw.net.requests.NullRequest> handle(Response<WindowResponse> windowResponse, Socket client) throws IOException {
            MatchEndPoint matchEndPoint = MatchEndPoint.getEndPointForMatch(windowResponse.getBody().getMatchId());
            
            
            return nullRequest(windowResponse);
        }
    
        @Override
        public boolean shouldBeKeptAlive() {
            return true;
        }
    }
}
