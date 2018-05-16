package it.polimi.ingsw.models;

import it.polimi.ingsw.core.locations.ChoosablePickLocation;
import java.util.LinkedList;

public class DraftPool implements ChoosablePickLocation {

    private LinkedList<Die> dice;
    private int players;
    private Bag bag;

    /**
     * Sets up a new {@link DraftPool} depending on the number of the players.
     * @param players the number of players.
     * @param bag the {@link Bag} used to take the count of dice.
     */
    public DraftPool(int players, Bag bag) {
        this.players = players;
        this.dice = new LinkedList<>();
        this.bag = bag;
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
        return this.dice.remove((int) location);
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

}
