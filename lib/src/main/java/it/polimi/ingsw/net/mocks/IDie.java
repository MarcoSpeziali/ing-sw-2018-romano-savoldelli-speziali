package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

public interface IDie extends JSONSerializable {
    
    @JSONElement("color")
    GlassColor getColor();
    @JSONElement("shade")
    Integer getShade();
}
