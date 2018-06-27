package it.polimi.ingsw.controllers.proxies.rmi;

import it.polimi.ingsw.controllers.CellController;
import it.polimi.ingsw.controllers.DieInteractionException;
import it.polimi.ingsw.controllers.WindowController;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.net.mocks.IDie;
import it.polimi.ingsw.net.mocks.IWindow;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

@SuppressWarnings("squid:S2160")
public class WindowRMIProxyController extends UnicastRemoteObject implements WindowController {
    
    private static final long serialVersionUID = 2002705957067372093L;
    
    private final transient WindowController proxiedController;
    
    private IWindow cachedWindow;
    private IWindow windowUpdate;
    private final transient Object updateSyncObject = new Object();
    
    public WindowRMIProxyController(IWindow window, WindowController proxiedController) throws RemoteException {
        super();
        
        this.cachedWindow = window;
        this.proxiedController = proxiedController;
    }
    
    @Override
    public IWindow getWindow() throws RemoteException {
        return this.cachedWindow;
    }
    
    @Override
    public CellController getCellController(int i, int j) throws RemoteException {
        return this.proxiedController.getCellController(i, j);
    }
    
    @Override
    public Die tryToPick(IDie die) throws RemoteException {
        return this.proxiedController.tryToPick(die);
    }
    
    @Override
    public Die tryToPick(Integer location) throws RemoteException {
        return this.proxiedController.tryToPick(location);
    }
    
    @Override
    public void tryToPut(IDie die, Integer location) throws RemoteException, DieInteractionException {
        this.proxiedController.tryToPut(die, location);
    }
    
    @Override
    public IWindow waitForUpdate() throws RemoteException, InterruptedException {
        synchronized (updateSyncObject) {
            while (getUpdate() == null) {
                // waits until there is a result
                updateSyncObject.wait();
            }
        
            return resetUpdate();
        }
    }
    
    private IWindow getUpdate() {
        synchronized (updateSyncObject) {
            return this.windowUpdate;
        }
    }
    
    private IWindow resetUpdate() {
        synchronized (updateSyncObject) {
            IWindow result = this.windowUpdate;
            this.windowUpdate = null;
            return result;
        }
    }
    
    @Override
    public void onUpdateReceived(IWindow update) throws RemoteException {
        synchronized (updateSyncObject) {
            this.windowUpdate = this.cachedWindow = update;
            
            final int rows = this.proxiedController.getWindow().getRows();
            final int columns = this.proxiedController.getWindow().getColumns();
    
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    this.proxiedController.getCellController(i, j).onUpdateReceived(update.getCells()[i][j]);
                }
            }
        
            updateSyncObject.notifyAll();
        }
    }
}
