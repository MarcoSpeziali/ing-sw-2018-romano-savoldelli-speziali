package it.polimi.ingsw.client.controllers;

import it.polimi.ingsw.controllers.CellController;
import it.polimi.ingsw.controllers.WindowController;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.net.mocks.IWindow;
import it.polimi.ingsw.net.mocks.WindowMock;

import java.rmi.RemoteException;

public class WindowMockController implements WindowController {
    private IWindow iWindow;

    public WindowMockController(IWindow iWindow) {
        this.iWindow = iWindow;
    }

    @Override
    public IWindow getWindow() throws RemoteException {
        return this.iWindow;
    }

    @Override
    public CellController getCellController(int i, int j) {
        //return this.iWindow.getCells()[i][j].getController();
        throw new UnsupportedOperationException();
    }

    @Override
    public Die tryToPick(Die die) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Die tryToPick(Integer integer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void tryToPut(Die die, Integer location) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onUpdateReceived(IWindow update) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IWindow waitForUpdate() {
        throw new UnsupportedOperationException();
    }
}
