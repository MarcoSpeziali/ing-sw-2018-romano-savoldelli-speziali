package it.polimi.ingsw.models;

import it.polimi.ingsw.core.locations.ChoosablePickLocation;
import it.polimi.ingsw.core.locations.ChoosablePutLocation;
import it.polimi.ingsw.listeners.OnDiePickedListener;
import it.polimi.ingsw.listeners.OnDiePutListener;
import it.polimi.ingsw.net.mocks.DieMock;
import it.polimi.ingsw.net.mocks.IDie;
import it.polimi.ingsw.net.mocks.IRoundTrack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RoundTrack implements ChoosablePutLocation, ChoosablePickLocation, IRoundTrack {
    
    private static final long serialVersionUID = -972922678430566496L;
    
    /**
     * The default number of rounds.
     */
    public static final byte DEFAULT_ROUND_NUMBER = 10;
    
    /**
     * The maximum number of rounds.
     */
    public static final byte MAX_ROUND_NUMBER = Byte.MAX_VALUE;
    
    /**
     * The minimum number of rounds.
     */
    public static final byte MIN_ROUND_NUMBER = 1;
    
    /**
     * The maximum number of discarded dice per round.
     */
    public static final byte MAX_NUMBER_OF_DICE_PER_ROUND = Byte.MAX_VALUE;
    
    /**
     * The number of rounds.
     */
    private final byte numberOfRounds;

    /**
     *
     */
    private transient List<OnDiePutListener> onDiePutListeners = new LinkedList<>();

    /**
     *
     */
    private transient List<OnDiePickedListener> onDiePickedListeners = new LinkedList<>();

    /**
     * Holds the dice discarded at the end of each round.
     */
    private List<List<Die>> rounds;

    /**
     * Initialized {@link RoundTrack} with the default value of rounds ({@link #DEFAULT_ROUND_NUMBER}).
     */
    public RoundTrack() {
        this(DEFAULT_ROUND_NUMBER);
    }

    /**
     * Initialized {@link RoundTrack} with a customized number of rounds.
     */
    public RoundTrack(byte numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
        if (numberOfRounds < MIN_ROUND_NUMBER) {
            throw new IllegalArgumentException(
                    "The number of rounds must be between " +
                            MIN_ROUND_NUMBER + " and " + MAX_ROUND_NUMBER +
                            ". Not " + numberOfRounds
            );
        }

        this.rounds = new LinkedList<>();

        // instantiates the round list
        for (int i = 0; i < numberOfRounds; i++) {
            this.rounds.add(new LinkedList<>());
        }
    }

    /**
     * @param round the round which contains the wanted dice
     * @return the {@link List} of {@link Die} discarded in the specified round
     */
    public List<Die> getDiceForRound(int round) {
        return this.rounds.get(round);
    }

    /**
     * @param round the round which contains the wanted die
     * @param index the index of the wanted die in the provided round
     * @return the {@link Die} discarded in the specified round at the specified index
     */
    public Die getDiceForRoundAtIndex(int round, int index) {
        return this.rounds.get(round).get(index);
    }

    /**
     * @param die   the die to discard
     * @param round the round of the discarded die
     * @param index the index of the discarded die of the provided round
     */
    public void setDieForRoundAtIndex(Die die, int round, int index) {
        // if the index does not exists then the die is added
        if (this.rounds.get(round).size() <= index) {
            this.rounds.get(round).add(die);

        }
        else {
            this.rounds.get(round).set(index, die);
        }

        this.onDiePutListeners.forEach(
                onDiePutListener -> onDiePutListener.onDiePut(die, computeLocation(round, index))
        );
    }

    /**
     * @param die   the die to add at the provided round
     * @param round the round index
     */
    public void addDieForRound(Die die, int round) {
        this.setDieForRoundAtIndex(die, round, MAX_NUMBER_OF_DICE_PER_ROUND);
    }

    @Override
    public Die pickDie(Die die) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Die pickDie(Integer location) {
        int roundIndex = (location & 0x0000FF00) >> 8;
        int dieIndex = location & 0x000000FF;

        Die die = this.rounds.get(roundIndex).remove(dieIndex);

        this.onDiePickedListeners.forEach(listener -> listener.onDiePicked(die, location));

        return die;
    }

    @Override
    public void putDie(Die die, Integer location) {
        int roundIndex = (location & 0x0000FF00) >> 8;
        int dieIndex = location & 0x000000FF;

        this.setDieForRoundAtIndex(die, roundIndex, dieIndex);
    }

    @Override
    public List<Integer> getLocations() {
        // an integer is a 4 byte number, the first two are set to 0
        // the third byte identifies the round index
        // the fourth one identifies the index of a die in a round

        List<Integer> positions = new LinkedList<>();

        for (int i = 0; i < this.rounds.size(); i++) {
            for (int j = 0; j < this.rounds.get(i).size(); j++) {
                positions.add(computeLocation(i, j));
            }
        }

        return positions;
    }

    @Override
    public List<Die> getDice() {
        return this.rounds.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public int getNumberOfDice() {
        return this.rounds.stream()
                .mapToInt(List::size)
                .sum();
    }

    @Override
    public int getFreeSpace() {
        return this.numberOfRounds * MAX_NUMBER_OF_DICE_PER_ROUND - this.getNumberOfDice();
    }

    /**
     * @param listener the listener to add
     * @return the provided instance of {@link OnDiePickedListener} so it can be unregistered later
     */
    public OnDiePickedListener addPickListener(OnDiePickedListener listener) {
        this.onDiePickedListeners.add(listener);

        return listener;
    }

    /**
     * @param listener the listener to add
     * @return the provided instance of {@link OnDiePutListener} so it can be unregistered later
     */
    public OnDiePutListener addPutListener(OnDiePutListener listener) {
        this.onDiePutListeners.add(listener);

        return listener;
    }

    /**
     * @param listener the listener to remove
     */
    public void removePickListener(OnDiePickedListener listener) {
        this.onDiePickedListeners.remove(listener);
    }

    /**
     * @param listener the listener to remove
     */
    public void removePutListener(OnDiePutListener listener) {
        this.onDiePutListeners.remove(listener);
    }

    /**
     * @param roundIndex the index of the round
     * @param dieIndex   the index of the die in the round
     * @return the location representation of the index: 0x0000{roundIndex}{dieIndex}
     */
    private int computeLocation(int roundIndex, int dieIndex) {
        return (roundIndex << 8 & 0x0000FF00) | (dieIndex & 0x000000FF);
    }
    
    @Override
    public Map<Integer, IDie> getLocationDieMap() {
        Map<Integer, IDie> integerDieMap = new HashMap<>();
    
        for (int i = 0; i < this.numberOfRounds; i++) {
            List<IDie> roundDice = this.getDiceForRound(i).stream()
                    .map(DieMock::new)
                    .collect(Collectors.toList());
    
            for (int j = 0; j < roundDice.size(); j++) {
                integerDieMap.put(
                        this.computeLocation(i, j),
                        roundDice.get(j)
                );
            }
        }
        
        return integerDieMap;
    }
    
    @Override
    public byte getNumberOfRounds() {
        return numberOfRounds;
    }
}
