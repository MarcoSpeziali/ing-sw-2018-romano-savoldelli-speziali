package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.locations.EmptyBagException;
import it.polimi.ingsw.core.locations.RandomPickLocation;
import it.polimi.ingsw.core.locations.RandomPutLocation;

import java.util.*;


public class Bag implements RandomPutLocation, RandomPickLocation {

    private Map<GlassColor, Integer> dice = new EnumMap<>(GlassColor.class);
    private List<GlassColor> colors;

    public Bag() {

        this.colors = new ArrayList<>(Arrays.asList(GlassColor.values()));

        for (GlassColor color : GlassColor.values()) {
            dice.put(color, 18);
        }
    }

    public Map<GlassColor, Integer> getDice() {
        return dice;
    }

    public int getNumberPerColor(GlassColor color) {
        return dice.get(color);
    }

    @Override
    public Die pickDie() {

        if (colors.isEmpty()) {
            throw new EmptyBagException("The bag has no dice left!");
        }
        Random rand = new Random();
        GlassColor randColor = colors.get(rand.nextInt(colors.size()));
        dice.merge(randColor, -1, Integer::sum);

        if (dice.get(randColor) == 0) {
            this.colors.remove(randColor);
        }

        return new Die(randColor, 0);
    }
    @Override
    public int getNumberOfDice() {
        return dice.values().stream().reduce((sum, x) -> (sum+x)).orElse(0);
    }

    @Override
    public void putDie(Die die) {
        dice.merge(die.getColor(), 1, Integer::sum);
    }

    @Override
    public int getFreeSpace() {
        return 90 - this.getNumberOfDice();
    }
}


