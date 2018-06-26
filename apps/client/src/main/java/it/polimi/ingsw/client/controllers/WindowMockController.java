package it.polimi.ingsw.client.controllers;

import it.polimi.ingsw.controllers.CellController;
import it.polimi.ingsw.controllers.WindowController;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.net.mocks.ICell;
import it.polimi.ingsw.net.mocks.IDie;
import it.polimi.ingsw.net.mocks.IWindow;

public class WindowMockController implements WindowController {
    private static final long serialVersionUID = 1911746152357955296L;
    private IWindow iWindow;

    public WindowMockController(IWindow iWindow) {
        this.iWindow = iWindow;
    }

    @Override
    public IWindow getWindow() {
        return this.iWindow;
    }

    @Override
    public CellController getCellController(int i, int j) {
        final ICell iCell = this.iWindow.getCells()[i][j];
        return new CellController() {
            private static final long serialVersionUID = 5858787543621651199L;

            @Override
            public ICell getCell() {
                return iCell;
            }

            @Override
            public Die tryToPick()  {
                throw new UnsupportedOperationException();
            }

            @Override
            public void tryToPut(IDie die) {
                throw new UnsupportedOperationException();
            }

            @Override
            public ICell waitForUpdate() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public Die tryToPick(IDie die) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Die tryToPick(Integer integer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void tryToPut(IDie die, Integer location) {
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
