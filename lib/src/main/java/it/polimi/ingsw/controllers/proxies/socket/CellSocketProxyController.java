package it.polimi.ingsw.controllers.proxies.socket;

import it.polimi.ingsw.controllers.CellController;
import it.polimi.ingsw.controllers.DieInteractionException;
import it.polimi.ingsw.net.mocks.ICell;
import it.polimi.ingsw.net.mocks.IDie;
import it.polimi.ingsw.net.providers.PersistentSocketInteractionProvider;

import java.rmi.RemoteException;

public class CellSocketProxyController implements CellController {
    
    private ICell cachedCell;
    private ICell cellUpdate;
    private final transient Object updateSyncObject = new Object();
    private final PersistentSocketInteractionProvider persistentSocketInteractionProvider;
    
    public CellSocketProxyController(ICell cell, PersistentSocketInteractionProvider persistentSocketInteractionProvider) {
        this.cachedCell = cell;
        this.persistentSocketInteractionProvider = persistentSocketInteractionProvider;
    }
    
    @Override
    public ICell getCell() throws RemoteException {
        return this.cachedCell;
    }
    
    @Override
    public IDie tryToPick() throws RemoteException {
        return null;
    }
    
    @Override
    public void tryToPut(IDie die) throws RemoteException, DieInteractionException {
    
    }
    
    @Override
    public ICell waitForUpdate() throws RemoteException, InterruptedException {
        return null;
    }
}
