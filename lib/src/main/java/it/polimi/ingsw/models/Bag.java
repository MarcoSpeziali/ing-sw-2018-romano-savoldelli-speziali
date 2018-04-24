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
    private int rand;

    private List<Die> getMyList () {

        List<Die> myList = new LinkedList<>();
        for (int i = 0; i < 3*GlassColor.values().length; i++) {
            for (int j = 0; j < 6; j++) {
                myList.add(new Die(GlassColor.values()[i], j));
            }
        }
        return myList;
    }

    public Bag() {
        this.dieList = getMyList();
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }


    @Override
    public Die pickDie() {


        rand = getRandomNumberInRange(1, 90);
        return dieList.remove(rand); //FIX this
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


