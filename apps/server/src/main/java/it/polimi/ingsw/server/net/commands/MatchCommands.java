package it.polimi.ingsw.server.net.commands;

import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.net.responses.NullResponse;
import it.polimi.ingsw.net.utils.EndPointFunction;
import it.polimi.ingsw.server.managers.MatchCommunicationsManager;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public final class MatchCommands {

    private MatchCommands() {}

    private static Map<Integer, MatchCommunicationsManager> matchCommunicationsManagerMap = new HashMap<>();

    @Handles(value = {EndPointFunction.MATCH_WINDOW_RESPONSE}, requiresAuthentication = true)
    public static final class NoResponseCommands implements Command<MatchInteraction, NullResponse> {

        @Override
        public Response<NullResponse> handle(Request<MatchInteraction> request, Socket client) throws IOException {
            return null;
        }
        @Override
        public boolean shouldBeKeptAlive() {
            return true;
        }

    }

    @Handles(value = {EndPointFunction.MATCH_MIGRATION}, requiresAuthentication = true)
    public static final class ResponseCommands implements Command<MatchInteraction, MatchInteraction> {

        @Override
        public Response<MatchInteraction> handle(Request<MatchInteraction> request, Socket client) throws IOException {
            return null;
        }

        @Override
        public boolean shouldBeKeptAlive() {
            return true;
        }
    }
}
