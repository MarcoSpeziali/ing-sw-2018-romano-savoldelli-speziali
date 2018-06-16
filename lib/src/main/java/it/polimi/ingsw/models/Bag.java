package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.locations.EmptyBagException;
import it.polimi.ingsw.core.locations.RandomPickLocation;
import it.polimi.ingsw.core.locations.RandomPutLocation;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


/**
 *
 */
public class Bag implements RandomPutLocation, RandomPickLocation {

    private Map<GlassColor, Integer> dice = new EnumMap<>(GlassColor.class);
    private List<GlassColor> colors;
    private int number;


    /**
     * Sets up a new {@link Bag} assigning a specified number of dice per color.
     *
     * @param number the number of dice per color.
     */
    public Bag(int number) {

        this.number = number;
        this.colors = new ArrayList<>(Arrays.asList(GlassColor.values()));

        for (GlassColor color : GlassColor.values()) {
            dice.put(color, number);
        }
    }

    /**
     * @return a {@link Map} representing the amount of dice left.
     */
    public Map<GlassColor, Integer> getDice() {
        return dice;
    }

    /**
     * @param color a {@link GlassColor} of which the amount is requested.
     * @return the amount of dice left of that color.
     */
    public int getNumberPerColor(GlassColor color) {
        return dice.get(color);
    }

    /**
     * Picks a random instance of die and decrements the initial amount of dice.
     *
     * @return a random incance of {@link Die}.
     */
    @Override
    public Die pickDie() {

        if (colors.isEmpty()) {
            throw new EmptyBagException("The bag has no dice left!");
        }
        int shade = ThreadLocalRandom.current().nextInt(1, 7);
        Random rand = new Random();
        GlassColor randColor = colors.get(rand.nextInt(colors.size()));
        dice.merge(randColor, -1, Integer::sum);

        if (dice.get(randColor) == 0) {
            this.colors.remove(randColor);
        }

        return new Die(randColor, shade);
    }

    /**
     * @return the total amount of dice left.
     */
    @Override
    public int getNumberOfDice() {
        return dice.values().stream().reduce((sum, x) -> (sum + x)).orElse(0);
    }

    /**
     * @param die the {@link Die} that must be put into.
     */
    @Override
    public void putDie(Die die) {
        dice.merge(die.getColor(), 1, Integer::sum);
    }

    /**
     * @return the amount of dice already removed.
     */
    @Override
    public int getFreeSpace() {
        return 5 * this.number - this.getNumberOfDice();
    }
}


