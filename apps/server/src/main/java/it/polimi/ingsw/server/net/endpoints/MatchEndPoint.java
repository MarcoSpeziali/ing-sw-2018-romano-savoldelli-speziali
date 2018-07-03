package it.polimi.ingsw.server.net.endpoints;

import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.controllers.proxies.rmi.MatchRMIProxyController;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.net.interfaces.MatchInterface;
import it.polimi.ingsw.net.mocks.IMatch;
import it.polimi.ingsw.net.mocks.PlayerMock;
import it.polimi.ingsw.server.Constants;
import it.polimi.ingsw.server.managers.LockManager;
import it.polimi.ingsw.server.managers.MatchManager;
import it.polimi.ingsw.server.net.sockets.AuthenticatedClientHandler;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.server.utils.AuthenticationHelper;
import it.polimi.ingsw.server.utils.ServerLogger;
import it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.wrap;

public class MatchEndPoint extends UnicastRemoteObject implements MatchInterface {

    private static Map<Integer, MatchEndPoint> endPointMap = new HashMap<>();
    public static MatchEndPoint getEndPointForMatch(IMatch match) throws RemoteException {
        return getEndPointForMatch(match.getId());
    }

    @SuppressWarnings("squid:S1602")
    public static synchronized MatchEndPoint getEndPointForMatch(int matchId) throws RemoteException {
        try {
            return endPointMap.computeIfAbsent(matchId, wrap(integer -> {
                return new MatchEndPoint(matchId);
            }));
        }
        catch (FunctionalExceptionWrapper e) {
            return e.tryFinalUnwrap(RemoteException.class);
        }
    }

    private int matchId;
    private transient MatchManager matchManager;

    public MatchEndPoint() throws RemoteException {
        super();
    }

    private MatchEndPoint(int matchId) throws RemoteException {
        this.matchId = matchId;
        this.matchManager = MatchManager.getManagerForMatch(matchId);
    }

    @Override
    public void confirmMatchJoin(Response<MatchInteraction> matchInteractionResponse) throws RemoteException {
        try {
            synchronized (LockManager.getLockObject(Constants.LockTargets.MATCH, this.matchId)) {
                DatabasePlayer databasePlayer = AuthenticationHelper.getAuthenticatedPlayer(matchInteractionResponse.getHeader().getClientToken());

                if (databasePlayer == null) {
                    return;
                }

                this.matchManager.addPlayer(databasePlayer, AuthenticatedClientHandler.getHandlerForPlayer(databasePlayer));
            }
        }
        catch (Exception e) {
            ServerLogger.getLogger().log(Level.SEVERE, "Error while confirming match join for request: " + matchInteractionResponse.toString(), e);
        }
    }

    @Override
    public MatchController confirmMatchJoinRMI(Response<MatchInteraction> matchInteractionResponse) {
        try {
            synchronized (LockManager.getLockObject(Constants.LockTargets.MATCH)) {
                this.matchId = matchInteractionResponse.getBody().getMatchId();
                this.matchManager = MatchManager.getManagerForMatch(this.matchId);
            }

            synchronized (LockManager.getLockObject(Constants.LockTargets.MATCH, this.matchId)) {
                DatabasePlayer databasePlayer = AuthenticationHelper.getAuthenticatedPlayer(matchInteractionResponse.getHeader().getClientToken());

                if (databasePlayer == null) {
                    return null;
                }

                MatchRMIProxyController matchRMIProxyController = new MatchRMIProxyController(new PlayerMock(databasePlayer));

                this.matchManager.addPlayer(databasePlayer, matchRMIProxyController);

                return matchRMIProxyController;
            }
        }
        catch (Exception e) {
            ServerLogger.getLogger().log(Level.SEVERE, "Error while confirming match join for request: " + matchInteractionResponse.toString(), e);
        }

        return null;
    }
}
