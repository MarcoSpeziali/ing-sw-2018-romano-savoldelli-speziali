package it.polimi.ingsw.controllers.proxies.rmi;

import it.polimi.ingsw.controllers.CellController;
import it.polimi.ingsw.controllers.DieInteractionException;
import it.polimi.ingsw.net.mocks.ICell;
import it.polimi.ingsw.net.mocks.IDie;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

@SuppressWarnings("squid:S2160")
public class CellRMIProxyController extends UnicastRemoteObject implements CellController {
    
    private static final long serialVersionUID = 2067176913023945636L;
    
    private ICell cachedCell;
    private ICell cellUpdate;
    private final transient Object updateSyncObject = new Object();
    
    private transient CellController proxiedController;
    
    public CellRMIProxyController(ICell cell, CellController proxiedController) throws RemoteException {
        super();
        
        this.cachedCell = cell;
        this.proxiedController = proxiedController;
    }
    
    @Override
    public ICell getCell() throws RemoteException {
        return this.cachedCell;
    }
    
    @Override
    public IDie tryToPick() throws RemoteException {
        return this.proxiedController.tryToPick();
    }
    
    @Override
    public void tryToPut(IDie die) throws RemoteException, DieInteractionException {
        this.proxiedController.tryToPut(die);
    }
    
    @Override
    public ICell waitForUpdate() throws RemoteException, InterruptedException {
        synchronized (updateSyncObject) {
            while (getUpdate() == null) {
                // waits until there is a result
                updateSyncObject.wait();
            }
        
            return resetUpdate();
        }
    }
    
    private ICell getUpdate() {
        synchronized (updateSyncObject) {
            return this.cellUpdate;
        }
    }
    
    private ICell resetUpdate() {
        synchronized (updateSyncObject) {
            ICell result = this.cellUpdate;
            this.cellUpdate = null;
            return result;
        }
    }
    
    @Override
    public void onUpdateReceived(ICell update) {
        synchronized (updateSyncObject) {
            this.cellUpdate = this.cachedCell = update;
        
            updateSyncObject.notifyAll();
        }
    }
}
