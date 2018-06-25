package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

import java.util.Map;

public interface IRoundTrack extends JSONSerializable {
    
    @JSONElement("number-of-rounds")
    byte getNumberOfRounds();
    
    @JSONElement("location-die-map")
    Map<Integer, IDie> getLocationDieMap();
}
