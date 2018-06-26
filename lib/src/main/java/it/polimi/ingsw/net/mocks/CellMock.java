package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class CellMock implements ICell {

    private static final long serialVersionUID = -1796852775563049154L;

    private GlassColor color;
    private Integer shade;
    private IDie die;

    // FIXME: we uagliò ci simme lamentàt ppe mis ro' fatto ca' o' costruttòr e' Die
    // FIXME: avessè nu' ordinè divèrs ra chello e' Cell e mo' o' invèrt e' nuovò? <3
    public CellMock(GlassColor color, Integer shade) {
        this.color = color;
        this.shade = shade;
    }

    public CellMock(ICell iCell) {
        this(iCell.getShade(), iCell.getColor(), new DieMock(iCell.getDie()));
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
}
