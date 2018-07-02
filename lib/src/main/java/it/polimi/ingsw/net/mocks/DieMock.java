package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

import java.util.Objects;

public class DieMock implements IDie {

    private static final long serialVersionUID = -6221553822460063755L;

    private GlassColor color;
    private Integer shade;

    public DieMock(IDie iDie) {
        this(iDie.getShade(), iDie.getColor());
    }

    @JSONDesignatedConstructor
    public DieMock(
            @JSONElement("shade") Integer shade,
            @JSONElement("color") GlassColor color
    ) {
        this.color = color;
        this.shade = shade;
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DieMock)) {
            return false;
        }
        DieMock dieMock = (DieMock) o;
        return color == dieMock.color &&
                Objects.equals(shade, dieMock.shade);
    }

    @Override
    public String toString() {
        return String.format("Die(%d, %s)", this.shade, this.color);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(color, shade);
    }
}
