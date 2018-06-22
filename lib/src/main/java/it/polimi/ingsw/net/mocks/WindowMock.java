package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

import java.util.Arrays;

public class WindowMock implements IWindow {

    private static final long serialVersionUID = -4356717552403331148L;

    private final String id;
    private final int difficulty;
    private final int rows;
    private final int columns;
    private final String siblingId;
    private final ICell[][] cells;

    public WindowMock(IWindow iWindow) {
        this(
                iWindow.getId(),
                iWindow.getDifficulty(),
                iWindow.getRows(),
                iWindow.getColumns(),
                iWindow.getSiblingId(),
                iWindow.getFlatCells()
        );
    }

    @JSONDesignatedConstructor
    public WindowMock(
            @JSONElement("id") String id,
            @JSONElement("difficulty") int difficulty,
            @JSONElement("rows") int rows,
            @JSONElement("columns") int columns,
            @JSONElement("sibling-id") String siblingId,
            @JSONElement("cells") ICell[] cells
    ) {
        this.id = id;
        this.difficulty = difficulty;
        this.rows = rows;
        this.columns = columns;
        this.siblingId = siblingId;

        this.cells = new Cell[rows][columns];

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
    @JSONElement("sibling-id")
    public String getSiblingId() {
        return this.siblingId;
    }

    @Override
    @JSONElement("cells")
    public ICell[] getFlatCells() {
        return Arrays.stream(this.cells)
                .flatMap(Arrays::stream)
                .toArray(ICell[]::new);
    }
}
