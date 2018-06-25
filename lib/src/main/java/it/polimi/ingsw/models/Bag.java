package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.locations.EmptyLocationException;
import it.polimi.ingsw.core.locations.RandomPickLocation;
import it.polimi.ingsw.core.locations.RandomPutLocation;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

import java.util.*;

public class Bag implements RandomPutLocation, RandomPickLocation, JSONSerializable {

    private static final long serialVersionUID = 6448459092375565034L;

    private Map<GlassColor, Integer> dice = new EnumMap<>(GlassColor.class);
    
    private transient List<GlassColor> colors;
    
    private int numberOfDicePerColor;
    
    private transient Random randomProvider;

    /**
     * Sets up a new {@link Bag} assigning a specified numberOfDicePerColor of dice per color.
     *
     * @param numberOfDicePerColor the number of dice per color.
     */
    public Bag(int numberOfDicePerColor) {
        this.randomProvider = new Random(System.currentTimeMillis());
        this.numberOfDicePerColor = numberOfDicePerColor;
        this.colors = new ArrayList<>(Arrays.asList(GlassColor.values()));

        Arrays.stream(GlassColor.values())
                .forEach(color -> dice.put(color, numberOfDicePerColor));
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
     * @return a random instance of {@link Die}.
     */
    @Override
    public Die pickDie() {
        if (colors.isEmpty()) {
            throw new EmptyLocationException(this);
        }

        int shade = randomProvider.nextInt(7) + 1;
        GlassColor randColor = colors.get(randomProvider.nextInt(colors.size()));

        dice.merge(randColor, -1, Integer::sum);

        if (dice.get(randColor) == 0) {
            this.colors.remove(randColor);
        }

        return new Die(shade, randColor);
    }

    /**
     * @return the total amount of dice left.
     */
    @Override
    public int getNumberOfDice() {
        return dice.values().stream()
                .reduce(Integer::sum)
                .orElse(0);
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
        return GlassColor.values().length * this.numberOfDicePerColor - this.getNumberOfDice();
    }
}


