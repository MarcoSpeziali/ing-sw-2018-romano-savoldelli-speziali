package it.polimi.ingsw.models;

import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.locations.RandomPickLocation;
import it.polimi.ingsw.core.locations.RandomPutLocation;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Bag implements RandomPutLocation, RandomPickLocation {

    private List<Die> dieList;

    private List<Die> getMyList () {

        List<Die> myList = new LinkedList<>();
        for (int i = 0; i < GlassColor.values().length; i++) {
            for (int j = 0; j < 3; j++) {
                myList.add(new Die(GlassColor.values()[i], j));
            }
        }
        return myList;
    }

    public Bag() {
        this.dieList = getMyList();
    }

    @Override
    public Die pickDie() {
        Random rand = new Random();
        return dieList.remove(rand.nextInt());
    }

    @Override
    public int getNumberOfDice() {
        return dieList.size();
    }

    @Override
    public void putDie(Die die) {
        this.dieList.add(die);
    }

    @Override
    public int getFreeSpace() {
        return 90 - dieList.size();
    }

}


