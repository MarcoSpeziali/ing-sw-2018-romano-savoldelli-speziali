package it.polimi.ingsw.models;

import it.polimi.ingsw.core.locations.ChoosablePickLocation;
import java.util.LinkedList;

public class DraftPool implements ChoosablePickLocation {

    private LinkedList<Die> dice;
    private int players;
    private Bag bag;

    public DraftPool(int players, Bag bag) {
        this.players = players;
        this.dice = new LinkedList<>();
        this.bag = bag;
        for (int i = 0; i < 2 * players +1; i++) {
            this.dice.add(bag.pickDie());
        }
    }

    @Override
    public Die pickDie(Die die) {
        for (int i = 0; i < dice.size() ; i++) {
            if(dice.get(i).equals(die)) {
                return dice.remove(i);
            }
        }
        return null;
    }

    @Override
    public Die pickDie(Integer location) {
        if (location < 0 || location > getNumberOfDice()) {
            throw new IndexOutOfBoundsException();
        }
        return this.dice.remove((int) location);
    }

    @Override
    public LinkedList<Integer> getLocations() {
        LinkedList<Integer> locations = new LinkedList<>();
        for (int i = 0; i < dice.size(); i++) {
            locations.add(i);
        }
        return locations;
    }

    @Override
    public LinkedList<Die> getDice() {
        return this.dice;
    }

    @Override
    public int getNumberOfDice() {
        return this.dice.size();
    }

}
