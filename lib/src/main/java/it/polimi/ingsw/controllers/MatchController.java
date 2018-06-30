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
    void waitForTurnToBegin() throws IOException;
    void endTurn() throws IOException;
    void waitForTurnToEnd() throws IOException;
    
    // ------ PLAYER MOVES ------
    MoveResponse tryToMove(Move move) throws IOException, InterruptedException;
    void requestToolCardUsage(IToolCard toolCard) throws IOException, NotEnoughTokensException;
    
        // ------ TOOL CARDS CALLBACK ------
        Map.Entry<JSONSerializable, Set<Integer>> waitForChooseDiePositionFromLocation();
        void postChosenDiePosition(Map.Entry<IDie, Integer> chosenPosition);
    
        Map.Entry<IEffect[], Range<Integer>> waitForChooseBetweenEffect();
        void postChosenEffects(IEffect[] effects);
        
        IEffect waitForContinueToRepeat();
        void postContinueToRepeatChoice(boolean continueToRepeat);
        
        IDie waitForSetShade();
        void postSetShade(Integer shade);
}
