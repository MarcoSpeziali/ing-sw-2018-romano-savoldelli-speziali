package it.polimi.ingsw.client.controllers;

import it.polimi.ingsw.controllers.DieController;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.net.mocks.IDie;

import java.rmi.RemoteException;

public class DieMockController implements DieController {
    private static final long serialVersionUID = -570673141141806349L;
    private IDie iDie;

    @Override
    public IDie getDie() throws RemoteException {
        return this.iDie;
    }

    @Override
    public void setShade(Integer shade) throws RemoteException {
        throw new UnsupportedOperationException();
    }

    public DieMockController(IDie iDie) {
        this.iDie = iDie;
    }

    @Override
    public Die waitForUpdate() throws RemoteException, InterruptedException {
        throw new UnsupportedOperationException();
    }
}
