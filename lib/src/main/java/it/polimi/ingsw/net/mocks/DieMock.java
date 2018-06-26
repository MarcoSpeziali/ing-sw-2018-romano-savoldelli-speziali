package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class DieMock implements IDie {

    private static final long serialVersionUID = -6221553822460063755L;

    private GlassColor color;
    private Integer shade;
    private final int uuid;

    public DieMock(IDie iDie) {
        this(iDie.getShade(), iDie.getColor(), iDie.getUUID());
    }

    @JSONDesignatedConstructor
    public DieMock(
            @JSONElement("shade") Integer shade,
            @JSONElement("color") GlassColor color,
            @JSONElement("uuid") int uuid
    ) {
        this.color = color;
        this.shade = shade;
        this.uuid = uuid;
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
    @JSONElement("uuid")
    public int getUUID() {
        return uuid;
    }
}
