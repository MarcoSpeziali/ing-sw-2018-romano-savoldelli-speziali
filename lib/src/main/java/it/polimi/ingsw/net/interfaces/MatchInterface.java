package it.polimi.ingsw.net.interfaces;

import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.utils.EndPointFunction;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MatchInterface extends Remote {

    @RespondsTo(EndPointFunction.MATCH_MIGRATION)
    void confirmMatchJoin(Response<MatchInteraction> matchInteractionResponse) throws RemoteException;

    @RespondsTo(EndPointFunction.MATCH_MIGRATION_RMI)
    MatchController confirmMatchJoinRMI(Response<MatchInteraction> matchInteractionResponse) throws RemoteException;
}
