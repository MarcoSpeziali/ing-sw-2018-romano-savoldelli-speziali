package it.polimi.ingsw.models;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Match;
import it.polimi.ingsw.core.locations.ChoosablePickLocation;
import it.polimi.ingsw.core.locations.FullLocationException;
import it.polimi.ingsw.core.locations.RandomPutLocation;
import it.polimi.ingsw.listeners.OnDiePickedListener;
import it.polimi.ingsw.listeners.OnDiePutListener;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DraftPool implements ChoosablePickLocation, RandomPutLocation, JSONSerializable {

    private static final long serialVersionUID = -7184365776887971173L;

    @JSONElement("dice-max")
    private final int maxNumberOfDice;

    @JSONElement("dice")
    private Die[] dice;

    private transient List<OnDiePutListener> onDiePutListeners = new LinkedList<>();
    private transient List<OnDiePickedListener> onDiePickedListeners = new LinkedList<>();

    /**
     * Sets up a new {@link DraftPool}.
     */
    public DraftPool(int maxNumberOfDice) {
        this.maxNumberOfDice = maxNumberOfDice;
        this.dice = new Die[maxNumberOfDice];
    }

    @JSONDesignatedConstructor
    DraftPool(
            @JSONElement("dice") Die[] dice,
            @JSONElement("dice-max") int maxNumberOfDice
    ) {
        this.maxNumberOfDice = maxNumberOfDice;
        this.dice = Arrays.copyOf(dice, dice.length);
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
        if (location < 0 || location > getNumberOfDice()) {
            throw new IndexOutOfBoundsException();
        }

        Die d = getAndRemove(dice, location);

        this.onDiePickedListeners.forEach(onDiePickedListener
                -> onDiePickedListener.onDiePicked(d));

        return d;
    }

    /**
     * @return a {@link LinkedList} of locations of dice.
     */
    @Override
    public List<Integer> getLocations() {
        LinkedList<Integer> locations = new LinkedList<>();

        for (int i = 0; i < dice.length; i++) {
            locations.add(i);
        }

        return locations;
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
        return this.dice.length;
    }

    @Override
    public void putDie(Die die) {
        for (int i = 0; i < this.dice.length; i++) {
            if (this.dice[i] == null) {
                dice[i] = die;

                this.onDiePutListeners.forEach(onDiePutListener
                        -> onDiePutListener.onDiePut(die));
            }
        }

        throw new FullLocationException(this);
    }

    @Override
    public int getFreeSpace() {
        int players = ((Match) Context.getSharedInstance().get(Context.MATCH)).getNumberOfPlayer();

        return 2 * players + 1 - this.dice.length;
    }

    public OnDiePutListener addPutListener(OnDiePutListener onDiePutListener) {
        this.onDiePutListeners.add(onDiePutListener);
        return onDiePutListener;
    }

    public OnDiePickedListener addPickListener(OnDiePickedListener onDiePickedListener) {
        this.onDiePickedListeners.add(onDiePickedListener);
        return onDiePickedListener;
    }

    private static Die getAndRemove(Die[] dice, int index) {
        Die toReturn = dice[index];
        dice[index] = null;
        return toReturn;
    }
}
