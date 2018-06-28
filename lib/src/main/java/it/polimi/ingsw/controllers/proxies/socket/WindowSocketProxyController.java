package it.polimi.ingsw.controllers.proxies.socket;

import it.polimi.ingsw.controllers.CellController;
import it.polimi.ingsw.controllers.DieInteractionException;
import it.polimi.ingsw.controllers.WindowController;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.net.mocks.IDie;
import it.polimi.ingsw.net.mocks.IWindow;
import it.polimi.ingsw.net.providers.PersistentSocketInteractionProvider;

import java.io.IOException;
import java.rmi.RemoteException;

public class WindowSocketProxyController implements WindowController {
    
    private static final long serialVersionUID = -5230643885024887031L;
    
    private IWindow cachedWindow;
    private IWindow windowUpdate;
    private final transient PersistentSocketInteractionProvider persistentSocketInteractionProvider;
    private final String clientToken;
    private final int matchId;
    private final transient Object updateSyncObject = new Object();
    
    public WindowSocketProxyController(IWindow window, PersistentSocketInteractionProvider persistentSocketInteractionProvider, String clientToken, int matchId) {
        this.cachedWindow = window;
        this.persistentSocketInteractionProvider = persistentSocketInteractionProvider;
        this.clientToken = clientToken;
        this.matchId = matchId;
        this.windowUpdate = null;
    }
    
    @Override
    public IWindow getWindow() throws RemoteException {
        return this.cachedWindow;
    }
    
    @Override
    public CellController getCellController(int i, int j) throws RemoteException {
        return null;
    }
    
    @Override
    public IDie tryToPick(IDie die) throws IOException {
        return null;
    }
    
    @Override
    public Die tryToPick(Integer location) throws RemoteException {
        return null;
    }
    
    @Override
    public void tryToPut(IDie die, Integer location) throws RemoteException, DieInteractionException {
    
    }
    
    @Override
    public IWindow waitForUpdate() throws RemoteException, InterruptedException {
        return null;
    }
}
