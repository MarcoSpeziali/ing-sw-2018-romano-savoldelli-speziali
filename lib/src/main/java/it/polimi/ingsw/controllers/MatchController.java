package it.polimi.ingsw.controllers;

import it.polimi.ingsw.controllers.proxies.ProxyUpdateInterface;
import it.polimi.ingsw.controllers.proxies.RemotelyClosable;
import it.polimi.ingsw.controllers.proxies.RemotelyInitializable;
import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.net.mocks.*;
import it.polimi.ingsw.net.responses.MoveResponse;
import it.polimi.ingsw.utils.Range;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

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
    void requestToolCardUsage(IToolCard toolCard) throws IOException;
    
        // ------ TOOL CARDS CALLBACK ------
            // set interi -> posizioni in cui non puo andare
            // if JSONS instanceof()
                // -- mostrare roundtrack
        Map.Entry<JSONSerializable, Set<Integer>> waitForChoosePositionFromLocation() throws IOException;
        void postChosenPosition(Integer chosenPosition) throws IOException;

            // range interi 2:5
        Map.Entry<IEffect[], Range<Integer>> waitForChooseBetweenEffect() throws IOException;
        void postChosenEffects(IEffect[] effects) throws IOException;

            // 0:n volte
        IEffect waitForContinueToRepeat() throws IOException;
        void postContinueToRepeatChoice(boolean continueToRepeat) throws IOException;

            //
        IDie waitForSetShade() throws IOException;
        void postSetShade(Integer shade) throws IOException;
    
    // ------ MATCH END ------
    IResult[] waitForMatchToEnd() throws IOException, InterruptedException;
}
