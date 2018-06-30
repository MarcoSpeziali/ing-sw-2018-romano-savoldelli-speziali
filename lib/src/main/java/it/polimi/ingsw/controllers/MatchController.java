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
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;

public interface MatchController extends ProxyUpdateInterface<IMatch>, RemotelyInitializable, RemotelyClosable {
    
    // ------ MATCH INITIALIZATION ------
    IWindow[] waitForWindowRequest() throws RemoteException, InterruptedException;
    void respondToWindowRequest(IWindow window) throws IOException;
    
    // ------ TURNS ------
    int waitForTurnToBegin() throws IOException, InterruptedException;
    void endTurn() throws IOException;
    void waitForTurnToEnd() throws IOException, InterruptedException;
    
    // ------ PLAYER MOVES ------
    MoveResponse tryToMove(Move move) throws IOException, InterruptedException;
    void requestToolCardUsage(IToolCard toolCard) throws IOException, NotEnoughTokensException;
    
        // ------ TOOL CARDS CALLBACK ------
            // set interi -> posizioni in cui non puo andare
            // if JSONS instanceof()
                // -- mostrare roundtrack
        Map.Entry<JSONSerializable, Set<Integer>> waitForChooseDiePositionFromLocation();
        void postChosenDiePosition(Map.Entry<IDie, Integer> chosenPosition);

            // range interi 2:5
        Map.Entry<IEffect[], Range<Integer>> waitForChooseBetweenEffect();
        void postChosenEffects(IEffect[] effects);

            // 0:n volte
        IEffect waitForContinueToRepeat();
        void postContinueToRepeatChoice(boolean continueToRepeat);

            //
        IDie waitForSetShade();
        void postSetShade(Integer shade);
    
    // ------ MATCH END ------
    Map<IPlayer, IResult> waitForMatchToEnd();
}
