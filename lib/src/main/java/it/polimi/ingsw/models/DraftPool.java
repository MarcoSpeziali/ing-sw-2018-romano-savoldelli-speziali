package it.polimi.ingsw.models;

import it.polimi.ingsw.core.locations.ChoosablePickLocation;
import it.polimi.ingsw.core.locations.FullLocationException;
import it.polimi.ingsw.core.locations.RandomPutLocation;
import it.polimi.ingsw.listeners.OnDiePickedListener;
import it.polimi.ingsw.listeners.OnDiePutListener;
import it.polimi.ingsw.net.mocks.DieMock;
import it.polimi.ingsw.net.mocks.IDie;
import it.polimi.ingsw.net.mocks.IDraftPool;

import java.util.*;
import java.util.stream.Collectors;

public class DraftPool implements ChoosablePickLocation, RandomPutLocation, IDraftPool {

    private static final long serialVersionUID = -7184365776887971173L;
    
    private final byte maxNumberOfDice;
    private Die[] dice;

    private transient List<OnDiePutListener> onDiePutListeners = new LinkedList<>();
    private transient List<OnDiePickedListener> onDiePickedListeners = new LinkedList<>();

    /**
     * Sets up a new {@link DraftPool}.
     */
    public DraftPool(byte numberOfPlayers) {
        this.maxNumberOfDice = (byte) (2 * numberOfPlayers + 1);
        this.dice = new Die[2 * numberOfPlayers + 1];
    }
    
    /**
     * Removes the die from the DraftPool and returns it.
     *
     * @param die the instance of {@link Die} to be removed.
     * @return the same instance of picked {@link Die}
     */
    @Override
    public Die pickDie(Die die) {
        for (int i = 0; i < dice.length; i++) {
            if (dice[i] == die) {
                this.onDiePutListeners.forEach(onDiePutListener
                        -> onDiePutListener.onDiePut(die));

                return getAndRemove(dice, i);
            }
        }

        this.onDiePutListeners.forEach(onDiePutListener
                -> onDiePutListener.onDiePut(null));

        return null;
    }

    /**
     * Removes the die from the DraftPool and returns it.
     *
     * @param location the index of the {@link Die} in {@link #dice}.
     * @return the same instance of picked {@link Die}
     */
    @Override
    public Die pickDie(Integer location) {
        if (location < 0 || location > this.dice.length) {
            throw new IndexOutOfBoundsException();
        }

        Die d = getAndRemove(dice, location);

        if (d == null) {
            throw new IndexOutOfBoundsException();
        }

        this.onDiePickedListeners.forEach(onDiePickedListener
                -> onDiePickedListener.onDiePicked(d));

        return d;
    }

    @Override
    public Die getDie(Integer location) {
        if (location < 0 || location > getNumberOfDice()) {
            throw new IndexOutOfBoundsException();
        }

        return dice[location];
    }

    /**
     * @return a {@link LinkedList} of locations of dice.
     */
    @Override
    public List<Integer> getFullLocations() {
        LinkedList<Integer> locations = new LinkedList<>();

        for (int i = 0; i < dice.length; i++) {
            if (dice[i] != null) {
                locations.add(i);
            }
        }

        return locations;
    }

    @Override
    public List<Integer> getEmptyLocations() {
        LinkedList<Integer> locations = new LinkedList<>();

        for (int i = 0; i < dice.length; i++) {
            if (dice[i] == null) {
                locations.add(i);
            }
        }

        return locations;
    }

    @Override
    public byte getMaxNumberOfDice() {
        return maxNumberOfDice;
    }
    
    @Override
    public Map<Integer, IDie> getLocationDieMap() {
        return this.getFullLocations().stream()
                .collect(Collectors.toMap(o -> o, o -> new DieMock(this.dice[o])));
    }
    
    /**
     * @return a {@link LinkedList} containing the dice.
     */
    @Override
    public List<Die> getDice() {
        return Arrays.asList(this.dice);
    }

    /**
     * @return the amount of dice left.
     */
    @Override
    public int getNumberOfDice() {
        return (int) Arrays.stream(this.dice).filter(Objects::nonNull).count();
    }

    @Override
    public void putDie(Die die) {
        for (int i = 0; i < this.dice.length; i++) {
            if (this.dice[i] == null) {
                dice[i] = die;

                this.onDiePutListeners.forEach(onDiePutListener
                        -> onDiePutListener.onDiePut(die));

                return;
            }
        }

        throw new FullLocationException(this);
    }

    @Override
    public int getFreeSpace() {
        return (int) Arrays.stream(this.dice).filter(Objects::isNull).count();
    }

    public void addPutListener(OnDiePutListener onDiePutListener) {
        this.onDiePutListeners.add(onDiePutListener);
    }

    public void addPickListener(OnDiePickedListener onDiePickedListener) {
        this.onDiePickedListeners.add(onDiePickedListener);
    }

    private static Die getAndRemove(Die[] dice, int index) {
        Die toReturn = dice[index];
        dice[index] = null;
        return toReturn;
    }
}
