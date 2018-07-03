package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;
import org.json.JSONObject;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DraftPoolMock implements IDraftPool {
    
    private static final long serialVersionUID = -926032206154241236L;
    
    private final byte maxNumberOfDice;
    private IDie[] dice;
    
    public DraftPoolMock(IDraftPool iDraftPool) {
        this(iDraftPool.getMaxNumberOfDice(), iDraftPool.getLocationDieMap());
    }
    
    public DraftPoolMock(byte maxNumberOfDice, IDie[] dice) {
        this.maxNumberOfDice = maxNumberOfDice;
        this.dice = dice;
    }

    @JSONDesignatedConstructor
    DraftPoolMock(
            @JSONElement("max-number-of-dice") byte maxNumberOfDice,
            @JSONElement(value = "location-die-map", keepRaw = true) JSONObject jsonObject
    ) {
        this.maxNumberOfDice = maxNumberOfDice;
        this.dice = new IDie[maxNumberOfDice];

        jsonObject.keySet().stream()
                .map(s -> Map.entry(
                        Integer.parseInt(s),
                        JSONSerializable.deserialize(
                                DieMock.class,
                                jsonObject.getJSONObject(s)
                        )
                )).forEach(entry -> this.dice[entry.getKey()] = entry.getValue());
    }
    
    public DraftPoolMock(byte maxNumberOfDice, Map<Integer, IDie> locationDieMap) {
        this.maxNumberOfDice = maxNumberOfDice;
        this.dice = new IDie[maxNumberOfDice];
        
        locationDieMap.forEach((location, iDie) -> this.dice[location] = iDie);
    }
    
    @Override
    @JSONElement("max-number-of-dice")
    public byte getMaxNumberOfDice() {
        return maxNumberOfDice;
    }
    
    @Override
    @JSONElement("location-die-map")
    public Map<Integer, IDie> getLocationDieMap() {
        return IntStream.range(0, this.maxNumberOfDice)
                .boxed()
                .filter(index -> this.dice[index] != null)
                .map(index -> Map.entry(index, new DieMock(this.dice[index])))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
