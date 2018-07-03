package it.polimi.ingsw.server.controllers;

import it.polimi.ingsw.controllers.DieInteractionException;
import it.polimi.ingsw.listeners.OnDiePickedListener;
import it.polimi.ingsw.listeners.OnDiePutListener;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.net.mocks.IDie;
import it.polimi.ingsw.server.events.EventDispatcher;
import it.polimi.ingsw.server.events.EventType;
import it.polimi.ingsw.server.events.ModelUpdateListener;

import java.util.Arrays;
import java.util.function.Function;

public class WindowControllerImpl implements OnDiePutListener, OnDiePickedListener {

    private static final long serialVersionUID = 6828316836970334881L;

    private Window window;
    private final CellControllerImpl[][] cellControllers;

    public WindowControllerImpl(Window window, Function<Cell, CellControllerImpl> cellControllerFunction) {
        this.window = window;
        this.cellControllers = new CellControllerImpl[window.getRows()][window.getColumns()];

        CellControllerImpl[] controllers = Arrays.stream(window.getCells())
                .flatMap(Arrays::stream)
                .map(cellControllerFunction)
                .toArray(CellControllerImpl[]::new);

        for (int i = 0; i < window.getRows(); i++) {
            System.arraycopy(controllers, i * window.getColumns(), this.cellControllers[i], 0, window.getColumns());
        }

        this.window.addPickListener(this);
        this.window.addPutListener(this);
    }

    public WindowControllerImpl(Window window, CellControllerImpl[][] cellControllers) {
        this.window = window;
        this.cellControllers = cellControllers;
        this.window.addPickListener(this);
        this.window.addPutListener(this);
    }

    public Window getWindow() {
        return this.window;
    }

    public CellControllerImpl getCellController(int i, int j) {
        return this.cellControllers[i][j];
    }

    public boolean canPutDieAtLocation(IDie die, Integer location) {
        return this.window.getPossiblePositionsForDie(
                new Die(die.getShade(), die.getColor()),
                false,
                false,
                false
        ).contains(location);
    }

    public Die tryToPick(int location) {
        int i = location / this.window.getColumns();
        int j = location % this.window.getColumns();

        return this.cellControllers[i][j].tryToPick();
    }

    public void tryToPut(Die die, int location, boolean ignoreColor, boolean ignoreShade, boolean ignoreAdjacency) throws DieInteractionException {
        if (this.window.getPossiblePositionsForDie(die, ignoreColor, ignoreShade, ignoreAdjacency).contains(location)) {
            int i = location / this.window.getColumns();
            int j = location % this.window.getColumns();

            this.cellControllers[i][j].tryToPut(die, ignoreColor, ignoreShade);
            return;
        }

        throw new DieInteractionException();
    }
    
    @Override
    public void onDiePicked(Die die, Integer location) {
        EventDispatcher.dispatch(
                EventType.MODEL_UPDATES,
                ModelUpdateListener.class,
                modelUpdateListener -> modelUpdateListener.onModelUpdated(this, this.window)
        );
    }
    
    @Override
    public void onDiePut(Die die, Integer location) {
        EventDispatcher.dispatch(
                EventType.MODEL_UPDATES,
                ModelUpdateListener.class,
                modelUpdateListener -> modelUpdateListener.onModelUpdated(this, this.window)
        );
    }
}
