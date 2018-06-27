package it.polimi.ingsw.server.net.endpoints;

import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.net.interfaces.MatchInterface;
import it.polimi.ingsw.net.mocks.IMatch;
import it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.wrap;

public class MatchEndPoint extends UnicastRemoteObject implements MatchInterface {

    private static Map<Integer, MatchEndPoint> endPointMap = new HashMap<>();
    
    public static MatchEndPoint getEndPointForMatch(IMatch match) throws RemoteException {
        return getEndPointForMatch(match.getId());
    }
    
    public static MatchEndPoint getEndPointForMatch(int matchId) throws RemoteException {
        try {
            return endPointMap.computeIfAbsent(matchId, wrap(integer -> {
                return new MatchEndPoint(matchId);
            }));
        }
        catch (FunctionalExceptionWrapper e) {
            return e.tryFinalUnwrap(RemoteException.class);
        }
    }
    
    private MatchEndPoint(int matchId) throws RemoteException {
    
    }

    @Override
    public void confirmMatchJoin(Response<MatchInteraction> matchInteractionResponse) throws RemoteException {
    }

    @Override
    public MatchController confirmMatchJoin(IMatch migrationResponse) {
        return null;
    }
}
