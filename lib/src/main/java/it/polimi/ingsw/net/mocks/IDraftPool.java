package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

import java.util.Map;

public interface IDraftPool extends JSONSerializable {
    
    @JSONElement("max-number-of-dice")
    byte getMaxNumberOfDice();
    
    @JSONElement("location-die-map")
    Map<Integer, IDie> getLocationDieMap();
}
