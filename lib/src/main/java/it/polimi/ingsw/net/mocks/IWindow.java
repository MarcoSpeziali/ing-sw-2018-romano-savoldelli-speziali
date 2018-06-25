package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

import java.util.List;

public interface IWindow extends JSONSerializable {

    @JSONElement("difficulty")
    int getDifficulty();
    @JSONElement("id")
    String getId();
    @JSONElement("rows")
    int getRows();
    @JSONElement("columns")
    int getColumns();
    @JSONElement("sibling-id")
    String getSiblingId();
    @JSONElement("cells")
    ICell[] getFlatCells();

    List<Integer> getPossiblePositionForDie();

    default ICell[][] getCells() {
        ICell[][] cells = new ICell[getRows()][getColumns()];

        for (int i = 0; i < getRows(); i++) {
            System.arraycopy(getFlatCells(), i * getColumns(), cells[i], 0, getColumns());
        }

        return cells;
    }
}
