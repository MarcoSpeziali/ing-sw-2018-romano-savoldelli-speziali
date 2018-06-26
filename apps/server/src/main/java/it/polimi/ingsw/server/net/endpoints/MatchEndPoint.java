package it.polimi.ingsw.server.net.endpoints;

import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.net.interfaces.MatchInterface;
import it.polimi.ingsw.net.mocks.IMatch;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MatchEndPoint extends UnicastRemoteObject implements MatchInterface {

    protected MatchEndPoint(IMatch iMatch) throws RemoteException {

    }

    @Override
    public Response<IMatch> confirmMatchJoin(Response<IMatch> migrationResponse) {
        return null;
    }

    @Override
    public MatchController confirmMatchJoin(IMatch migrationResponse) {
        return null;
    }
}
