package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

import java.util.Arrays;
import java.util.List;

public class WindowMock implements IWindow {

    private static final long serialVersionUID = -1892439094035073607L;

    private final String id;
    private final int difficulty;
    private final int rows;
    private final int columns;
    private final ICell[][] cells;

    public WindowMock(IWindow iWindow) {
        this(
                iWindow.getId(),
                iWindow.getDifficulty(),
                iWindow.getRows(),
                iWindow.getColumns(),
                Arrays.stream(iWindow.getFlatCells())
                        .map(CellMock::new)
                        .toArray(CellMock[]::new)
        );
    }

    @JSONDesignatedConstructor
    public WindowMock(
            @JSONElement("id") String id,
            @JSONElement("difficulty") int difficulty,
            @JSONElement("rows") int rows,
            @JSONElement("columns") int columns,
            @JSONElement("cells") CellMock[] cells
    ) {
        this.id = id;
        this.difficulty = difficulty;
        this.rows = rows;
        this.columns = columns;

        this.cells = new ICell[rows][columns];
        
        for (int i = 0; i < rows; i++) {
            System.arraycopy(cells, i * columns, this.cells[i], 0, columns);
        }
    }


    @Override
    @JSONElement("difficulty")
    public int getDifficulty() {
        return this.difficulty;
    }

    @Override
    @JSONElement("id")
    public String getId() {
        return this.id;
    }

    @Override
    @JSONElement("rows")
    public int getRows() {
        return this.rows;
    }

    @Override
    @JSONElement("columns")
    public int getColumns() {
        return this.columns;
    }

    @Override
    @JSONElement("cells")
    public ICell[] getFlatCells() {
        return Arrays.stream(this.cells)
                .flatMap(Arrays::stream)
                .toArray(ICell[]::new);
    }

}
