package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

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
    public DraftPoolMock(
            @JSONElement("max-number-of-dice") byte maxNumberOfDice,
            @JSONElement("location-die-map") Map<Integer, IDie> locationDieMap
    ) {
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
                .collect(Collectors.toMap(o -> o, o -> new DieMock(this.dice[o])));
    }
}
