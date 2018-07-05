package it.polimi.ingsw.controllers;

import it.polimi.ingsw.controllers.proxies.ProxyUpdateInterface;
import it.polimi.ingsw.controllers.proxies.RemotelyClosable;
import it.polimi.ingsw.controllers.proxies.RemotelyInitializable;
import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.net.mocks.*;
import it.polimi.ingsw.net.requests.ChooseBetweenActionsRequest;
import it.polimi.ingsw.net.requests.ChoosePositionForLocationRequest;
import it.polimi.ingsw.net.responses.MoveResponse;

import java.io.IOException;

public interface MatchController extends ProxyUpdateInterface<IMatch>, RemotelyInitializable, RemotelyClosable {
    
    // ------ MATCH INITIALIZATION ------
    IWindow[] waitForWindowRequest() throws IOException, InterruptedException;
    void respondToWindowRequest(IWindow window) throws IOException;
    
    // ------ TURNS ------
    int waitForTurnToBegin() throws IOException, InterruptedException;
    void endTurn() throws IOException;
    void waitForTurnToEnd() throws IOException, InterruptedException;
    
    // ------ PLAYER MOVES ------
    MoveResponse tryToMove(Move move) throws IOException, InterruptedException;
    void requestToolCardUsage(IToolCard toolCard) throws IOException, InterruptedException;
    
        // ------ TOOL CARDS CALLBACK ------
            // set interi -> posizioni in cui non puo andare
            // if JSONS instanceof()
                // -- mostrare roundtrack
        ChoosePositionForLocationRequest waitForChoosePositionFromLocation() throws IOException, InterruptedException;
        void postChosenPosition(Integer chosenPosition) throws IOException;

            // range interi 2:5
        ChooseBetweenActionsRequest waitForChooseBetweenActions() throws IOException, InterruptedException;
        void postChosenActions(IAction[] actions) throws IOException;

            // 0:n volte
        IAction waitForContinueToRepeat() throws IOException, InterruptedException;
        void postContinueToRepeatChoice(boolean continueToRepeat) throws IOException;

            //
        IDie waitForSetShade() throws IOException, InterruptedException;
        void postSetShade(Integer shade) throws IOException;
    
    // ------ MATCH END ------
    IResult[] waitForMatchToEnd() throws IOException, InterruptedException;
}
