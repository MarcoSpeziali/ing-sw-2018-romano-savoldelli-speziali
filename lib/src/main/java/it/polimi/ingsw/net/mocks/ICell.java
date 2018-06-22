package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

public interface ICell extends JSONSerializable {

    @JSONElement("color")
    GlassColor getColor();
    @JSONElement("shade")
    Integer getShade();
    @JSONElement("die")
    IDie getDie();

    default boolean isOccupied() {
        return this.getDie() != null;
    }

    default boolean isBlank() {
        return this.getColor() == null && this.getShade() == 0;
    }
}
