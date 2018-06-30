package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

import java.util.Objects;

public class CellMock implements ICell {

    private static final long serialVersionUID = -1796852775563049154L;
    
    private GlassColor color;
    private Integer shade;
    private IDie die;
    
    public CellMock(ICell iCell) {
        this(
                iCell.getShade(),
                iCell.getColor(),
                iCell.getDie() == null ?
                        null :
                        new DieMock(iCell.getDie())
        );
    }
    
    public CellMock(Integer shade, GlassColor color) {
        this(shade, color, null);
    }

    @JSONDesignatedConstructor
    public CellMock(
            @JSONElement("shade") int shade,
            @JSONElement("color") GlassColor color,
            @JSONElement("die") DieMock die
    ) {
        this.shade = shade;
        this.color = color;
        this.die = die;
    }

    @Override
    @JSONElement("color")
    public GlassColor getColor() {
        return this.color;
    }

    @Override
    @JSONElement("shade")
    public Integer getShade() {
        return this.shade;
    }

    @Override
    @JSONElement("die")
    public IDie getDie() {
        return this.die;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CellMock)) {
            return false;
        }
        CellMock cellMock = (CellMock) o;
        return color == cellMock.color &&
                Objects.equals(shade, cellMock.shade) &&
                Objects.equals(die, cellMock.die);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(color, shade, die);
    }
}
