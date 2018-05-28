package it.polimi.ingsw.models;

import it.polimi.ingsw.core.locations.ChoosablePickLocation;
import it.polimi.ingsw.core.locations.RandomPutLocation;
import it.polimi.ingsw.listeners.OnDiePickedListener;
import it.polimi.ingsw.listeners.OnDiePutListener;
import it.polimi.ingsw.views.DraftPoolView;

import java.util.LinkedList;
import java.util.List;

public class DraftPool implements ChoosablePickLocation, RandomPutLocation {

    private LinkedList<Die> dice;
    private int players;
    private Bag bag;
    private DraftPoolView draftPoolView;
    private List<OnDiePutListener> onDiePutListeners =  new LinkedList<>();
    private List<OnDiePickedListener> onDiePickedListeners = new LinkedList<>();


    /**
     * Sets up a new {@link DraftPool} depending on the number of the players.
     * @param players the number of players.
     * @param bag the {@link Bag} used to take the count of dice.
     */

    public DraftPool(int players, DraftPoolView draftPoolView, Bag bag) {
        this.players = players;
        this.dice = new LinkedList<>();
        this.bag = bag;
        this.draftPoolView = draftPoolView;
        for (int i = 0; i < 2 * players +1; i++) {
            this.dice.add(bag.pickDie());
        }
    }

    /**
     * Removes the die from the DraftPool and returns it.
     * @param die the instance of {@link Die} to be removed.
     * @return the same instance of picked {@link Die}
     */
    @Override
    public Die pickDie(Die die) {
        for (int i = 0; i < dice.size() ; i++) {
            if(dice.get(i) == die) {
                return dice.remove(i);
            }
        }
        this.onDiePutListeners.forEach(onDiePutListener
                -> onDiePutListener.onDiePut(die));
        draftPoolView.render();
        return null;
    }

    /**
     * Removes the die from the DraftPool and returns it.
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

        draftPoolView.render();
        return d;
    }

    /**
     * @return a {@link LinkedList} of locations of dice.
     */
    @Override
    public LinkedList<Integer> getLocations() {
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
    public LinkedList<Die> getDice() {
        return this.dice;
    }
    /**
     * @return the amount of dice left.
     */
    @Override
    public int getNumberOfDice() {
        return this.dice.size();
    }

    /**
     * @param players is the number of players
     */
    public void setPlayers(int players) {
        this.players = players;
    }

    /**
     * @param bag set the bag {@link Bag} to take the count of dice
     */
    public void setBag(Bag bag) {
        this.bag = bag;
    }

    /**
     * @return the number of players
     */
    public int getPlayers() {

        return this.players;
    }

    /**
     * @return the {@link Bag} used to take the count of dice.
     */
    public Bag getBag() {
        return this.bag;
    }

    @Override
    public void putDie(Die die) {
        // TODO check free space
        dice.add(die);
        this.onDiePutListeners.forEach(onDiePutListener
                -> onDiePutListener.onDiePut(die));
    }

    @Override
    public int getFreeSpace() {

        return ((2*players+1) - ((2*players + 1) - this.dice.size()));
    }

    public void addPutListener(OnDiePutListener onDiePutListener) {
        this.onDiePutListeners.add(onDiePutListener);
    }

    public void addPickListener(OnDiePickedListener onDiePickedListener) {
        this.onDiePickedListeners.add(onDiePickedListener);
    }
}
