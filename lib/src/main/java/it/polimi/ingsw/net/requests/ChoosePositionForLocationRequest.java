package it.polimi.ingsw.net.requests;

import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;
import org.json.JSONObject;

import java.util.Set;

public class ChoosePositionForLocationRequest implements MatchInteraction {
    
    private static final long serialVersionUID = -5713281095261369147L;
    
    private final int matchId;
    private final JSONSerializable location;
    private final Set<Integer> unavailableLocations;
    
    public ChoosePositionForLocationRequest(int matchId, JSONSerializable location, Set<Integer> unavailableLocations) {
        this.matchId = matchId;
        this.location = location;
        this.unavailableLocations = unavailableLocations;
    }
    
    @JSONDesignatedConstructor
    ChoosePositionForLocationRequest(
            @JSONElement("match-id") int matchId,
            @JSONElement(value = "location", keepRaw = true) JSONObject locationJSONObject,
            @JSONElement("unavailable-locations") Set<Integer> unavailableLocations
    ) throws ClassNotFoundException {
        //noinspection unchecked
        this(
                matchId,
                JSONSerializable.deserialize(
                        (Class<? extends JSONSerializable>) Class.forName(locationJSONObject.getString("class")),
                        locationJSONObject
                ),
                unavailableLocations
        );
    }
    
    @Override
    @JSONElement("match-id")
    public int getMatchId() {
        return matchId;
    }
    
    @JSONElement("unavailable-locations")
    public Set<Integer> getUnavailableLocations() {
        return unavailableLocations;
    }
    
    @JSONElement("location")
    public JSONSerializable getLocation() {
        return location;
    }
    
    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = MatchInteraction.super.serialize();
        
        jsonObject.getJSONObject("location")
                .put("class", this.location.getClass().getName());
        
        return jsonObject;
    }
}
