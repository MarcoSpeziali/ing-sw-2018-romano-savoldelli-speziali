package it.polimi.ingsw.net.interfaces;

import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.mocks.IMatch;
import it.polimi.ingsw.net.utils.EndPointFunction;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MatchInterface extends Remote {

    @RespondsTo(EndPointFunction.MATCH_MIGRATION)
    Response<IMatch> confirmMatchJoin(Response<IMatch> migrationResponse) throws RemoteException;

    @RespondsTo(EndPointFunction.MATCH_MIGRATION_RMI)
    MatchController confirmMatchJoin(IMatch migrationResponse) throws RemoteException;
}
