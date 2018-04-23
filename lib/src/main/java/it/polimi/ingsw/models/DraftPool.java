package it.polimi.ingsw.models;

import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.locations.ChoosablePickLocation;
import it.polimi.ingsw.core.locations.RandomPutLocation;

import java.util.List;

// FIXME: DraftPool dovrebbe implementare ChoosablePickLocation, RandomPutLocation
public class DraftPool implements ChoosablePickLocation, RandomPutLocation{
    private Die[] dieArray;

    @Override
    public Die pickDie(Die die) {

    }

    @Override
    public Die pickDie(Integer location) {
        return null;
    }

    @Override
    public List<Integer> getLocations() {
        return null;
    }

    @Override
    public List<Die> getDice() {
        return null;3
    }

    @Override
    public int getNumberOfDice() {
        return 0;
    }

    @Override
    public void putDie(Die die) {

    }

    @Override
    public int getFreeSpace() {
        return 0;
    }
}

