package it.polimi.ingsw.models;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Match;
import it.polimi.ingsw.core.locations.ChoosablePickLocation;
import it.polimi.ingsw.core.locations.RandomPutLocation;
import it.polimi.ingsw.listeners.OnDiePickedListener;
import it.polimi.ingsw.listeners.OnDiePutListener;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

import java.util.LinkedList;
import java.util.List;

public class DraftPool implements ChoosablePickLocation, RandomPutLocation, JSONSerializable {

    private static final long serialVersionUID = -7184365776887971173L;

    @JSONElement("dice")
    private LinkedList<Die> dice;

    private transient List<OnDiePutListener> onDiePutListeners = new LinkedList<>();
    private transient List<OnDiePickedListener> onDiePickedListeners = new LinkedList<>();


    /**
     * Sets up a new {@link DraftPool}.
     */
    public DraftPool() {
        this.dice = new LinkedList<>();
    }

    @JSONDesignatedConstructor
    DraftPool(@JSONElement("dice") List<Die> dice) {
        this.dice = new LinkedList<>(dice);
    }
    
    /**
     * Removes the die from the DraftPool and returns it.
     *
     * @param die the instance of {@link Die} to be removed.
     * @return the same instance of picked {@link Die}
     */

    @Override
    public Die pickDie(Die die) {
        for (int i = 0; i < dice.size(); i++) {
            if (dice.get(i) == die) {
                this.onDiePutListeners.forEach(onDiePutListener
                        -> onDiePutListener.onDiePut(die));

                return dice.remove(i);
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

        Die d = this.dice.remove((int) location);

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

        for (int i = 0; i < dice.size(); i++) {
            locations.add(i);
        }

        return locations;
    }

    /**
     * @return a {@link LinkedList} containing the dice.
     */
    @Override
    public List<Die> getDice() {
        return this.dice;
    }

    /**
     * @return the amount of dice left.
     */
    @Override
    public int getNumberOfDice() {
        return this.dice.size();
    }

    @Override
    public void putDie(Die die) {
        dice.add(die);

        this.onDiePutListeners.forEach(onDiePutListener
                -> onDiePutListener.onDiePut(die));
    }

    @Override
    public int getFreeSpace() {
        int players = ((Match) Context.getSharedInstance().get(Context.MATCH)).getNumberOfPlayer();

        return 2 * players + 1 - this.dice.size();
    }

    public OnDiePutListener addPutListener(OnDiePutListener onDiePutListener) {
        this.onDiePutListeners.add(onDiePutListener);
        return onDiePutListener;
    }

    public OnDiePickedListener addPickListener(OnDiePickedListener onDiePickedListener) {
        this.onDiePickedListeners.add(onDiePickedListener);
        return onDiePickedListener;
    }
}
