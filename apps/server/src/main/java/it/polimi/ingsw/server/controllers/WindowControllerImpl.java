package it.polimi.ingsw.server.controllers;

import it.polimi.ingsw.controllers.DieInteractionException;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.Window;

import java.util.Arrays;
import java.util.function.Function;

public class WindowControllerImpl {

    private final Window window;
    private final CellControllerImpl[][] cellControllers;

    public WindowControllerImpl(Window window, Function<Cell, CellControllerImpl> cellControllerFunction) {
        this(
                window,
                Arrays.stream(window.getCells())
                        .flatMap(Arrays::stream)
                        .map(cellControllerFunction)
                        .map(cellController -> new CellControllerImpl[] { cellController })
                        .toArray(CellControllerImpl[][]::new)
        );
    }

    public WindowControllerImpl(Window window, CellControllerImpl[][] cellControllers) {
        this.window = window;
        this.cellControllers = cellControllers;
    }

    public Window getWindow() {
        return this.window;
    }

    public CellControllerImpl getCellController(int i, int j) {
        return this.cellControllers[i][j];
    }

    public Die tryToPick(Die die) {
        for (CellControllerImpl[] controllers : this.cellControllers) {
            for (CellControllerImpl cellController : controllers) {
                Die cellDie = cellController.getCell().getDie();

                if (cellDie != null && cellDie.equals(die)) {
                    return cellController.tryToPick();
                }
            }
        }

        throw new IllegalArgumentException();
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
}
