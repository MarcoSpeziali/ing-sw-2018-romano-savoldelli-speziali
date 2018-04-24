package it.polimi.ingsw.models;

import it.polimi.ingsw.core.locations.ChoosablePickLocation;

import java.util.LinkedList;
import java.util.List;

public class DraftPool implements ChoosablePickLocation {

    private final LinkedList<Die> dice;

    public DraftPool(int players) {

        this.dice = new LinkedList<>();
        Bag bag = new Bag();
        for (int i = 0; i < 2 * players + 1; i++) {
            this.dice.add(bag.pickDie());
        }

    }

    @Override
    public Die pickDie(Die die) {
        if (this.dice.contains(die)) {
            int idx = this.dice.indexOf(die);
            return this.dice.remove(idx);
        } else return null;
    }

    @Override
    public Die pickDie(Integer location) {
        return this.dice.get(location);
    }

    @Override
    // TODO CHECKME!
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
