package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utils.streams.StreamUtils.invertBiConsumer;

public class RoundTrackMock implements IRoundTrack {
    
    private static final long serialVersionUID = -688690890944587360L;
    
    private final byte numberOfRounds;
    private final List<List<IDie>> rounds;
    
    public RoundTrackMock(byte numberOfRounds, List<List<IDie>> rounds) {
        this.numberOfRounds = numberOfRounds;
        this.rounds = rounds;
    }
    
    public RoundTrackMock(IRoundTrack iRoundTrack) {
        this(iRoundTrack.getNumberOfRounds(), iRoundTrack.getLocationDieMap().entrySet().stream()
                .map(integerIDieEntry -> Map.entry(
                        integerIDieEntry.getKey(),
                        new DieMock(integerIDieEntry.getValue()))
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    public RoundTrackMock(byte numberOfRounds, Map<Integer, DieMock> locationDieMap) {
        this(numberOfRounds, new LinkedList<>());

        for (int i = 0; i < numberOfRounds; i++) {
            this.rounds.add(new LinkedList<>());
        }

        locationDieMap.forEach(invertBiConsumer(this::putDie));
    }
    
    @JSONDesignatedConstructor
    RoundTrackMock(
            @JSONElement("round-number") byte numberOfRounds,
            @JSONElement(value = "location-die-map", keepRaw = true) JSONObject jsonObject
    ) {
        this(numberOfRounds, new LinkedList<>());

        for (int i = 0; i < numberOfRounds; i++) {
            this.rounds.add(new LinkedList<>());
        }

        jsonObject.keySet().stream()
                .map(s -> Map.entry(
                        Integer.parseInt(s),
                        JSONSerializable.deserialize(
                                DieMock.class,
                                jsonObject.getJSONObject(s)
                        )
                )).forEach(entry -> this.putDie(entry.getValue(), entry.getKey()));
    }
    
    @Override
    @JSONElement("round-number")
    public byte getNumberOfRounds() {
        return numberOfRounds;
    }
    
    @Override
    @JSONElement("location-die-map")
    public Map<Integer, IDie> getLocationDieMap() {
        Map<Integer, IDie> integerDieMap = new HashMap<>();
    
        for (int i = 0; i < this.rounds.size(); i++) {
            List<IDie> roundDice = this.getDiceForRound(i);
        
            for (int j = 0; j < roundDice.size(); j++) {
                integerDieMap.put(
                        this.computeLocation(i, j),
                        roundDice.get(j)
                );
            }
        }
    
        return integerDieMap;
    }
    
    private void putDie(IDie die, Integer location) {
        int roundIndex = (location & 0x0000FF00) >> 8;
        int dieIndex = location & 0x000000FF;
        
        this.setDieForRoundAtIndex(die, roundIndex, dieIndex);
    }
    
    /**
     * @param round the round which contains the wanted dice
     * @return the {@link List} of {@link Die} discarded in the specified round
     */
    private List<IDie> getDiceForRound(int round) {
        return this.rounds.get(round).stream()
                .map(DieMock::new)
                .collect(Collectors.toList());
    }
    
    /**
     * @param roundIndex the index of the round
     * @param dieIndex   the index of the die in the round
     * @return the location representation of the index: 0x0000{roundIndex}{dieIndex}
     */
    private int computeLocation(int roundIndex, int dieIndex) {
        return (roundIndex << 8 & 0x0000FF00) | (dieIndex & 0x000000FF);
    }
    
    /**
     * @param die   the die to discard
     * @param round the round of the discarded die
     * @param index the index of the discarded die of the provided round
     */
    public void setDieForRoundAtIndex(IDie die, int round, int index) {
        // if the index does not exists then the die is added
        if (this.rounds.get(round).size() <= index) {
            this.rounds.get(round).add(die);
            
        }
        else {
            this.rounds.get(round).set(index, die);
        }
    }
}
