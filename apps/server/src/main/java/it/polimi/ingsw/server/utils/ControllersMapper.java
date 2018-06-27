package it.polimi.ingsw.server.utils;

import it.polimi.ingsw.controllers.CellController;
import it.polimi.ingsw.controllers.DieInteractionException;
import it.polimi.ingsw.controllers.WindowController;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.net.mocks.*;
import it.polimi.ingsw.server.controllers.CellControllerImpl;
import it.polimi.ingsw.server.controllers.WindowControllerImpl;
import it.polimi.ingsw.server.managers.MatchManager;

import java.rmi.RemoteException;

public final class ControllersMapper {
    private ControllersMapper() {}
    
    public static CellController map(CellControllerImpl cellController, MatchManager matchManager) {
        return new CellController() {
            private static final long serialVersionUID = 5423042505658265001L;
    
            @Override
            public ICell getCell() throws RemoteException {
                return new CellMock(cellController.getCell());
            }
    
            @Override
            public IDie tryToPick() throws RemoteException {
                return new DieMock(cellController.tryToPick());
            }
    
            @Override
            public void tryToPut(IDie die) throws RemoteException, DieInteractionException {
                cellController.tryToPut(
                        Die.getDieWithUUID(die.getUUID()),
                        matchManager.shouldIgnoreColor(),
                        matchManager.shouldIgnoreShade()
                );
            }
    
            @Override
            public ICell waitForUpdate() throws RemoteException, InterruptedException {
                return new CellMock(cellController.waitForUpdate());
            }
        };
    }
    
    public static WindowController map(WindowControllerImpl windowController, CellController[][] cellControllers, MatchManager matchManager) {
        return new WindowController() {
            private static final long serialVersionUID = 4502486733968696498L;
    
            @Override
            public IWindow getWindow() throws RemoteException {
                return new WindowMock(windowController.getWindow());
            }
    
            @Override
            public CellController getCellController(int i, int j) throws RemoteException {
                return cellControllers[i][j];
            }
    
            @Override
            public Die tryToPick(IDie die) throws RemoteException {
                Die actualDie = Die.getDieWithUUID(die.getUUID());
                return windowController.tryToPick(actualDie);
            }
    
            @Override
            public Die tryToPick(Integer location) throws RemoteException {
                return windowController.tryToPick(location);
            }
    
            @Override
            public void tryToPut(IDie die, Integer location) throws RemoteException, DieInteractionException {
                Die actualDie = Die.getDieWithUUID(die.getUUID());
                
                windowController.tryToPut(
                        actualDie,
                        location,
                        matchManager.shouldIgnoreColor(),
                        matchManager.shouldIgnoreShade(),
                        matchManager.shouldIgnoreAdjacency()
                );
            }
    
            @Override
            public IWindow waitForUpdate() throws RemoteException, InterruptedException {
                return windowController.waitForUpdate();
            }
        };
    }
}
