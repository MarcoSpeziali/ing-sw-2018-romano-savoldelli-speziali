package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.actions.VariableSupplier;
import it.polimi.ingsw.models.Die;

import java.util.List;

public class CountDiceInstruction extends Instruction {

    /**
     * A {@link VariableSupplier} that provides the list of dice to count.
     */
    private final VariableSupplier<List<Die>> diceSupplier;

    /**
     * The filter for the dice shade.
     */
    private final Integer filterShade;

    /**
     * The filter for the dice color.
     */
    private final GlassColor filterColor;

    public CountDiceInstruction(VariableSupplier<List<Die>> diceSupplier, Integer filterShade, GlassColor filterColor) {
        this.diceSupplier = diceSupplier;
        this.filterShade = filterShade;
        this.filterColor = filterColor;
    }

    @Override
    public Integer run(Context context) {
        // Returns the count of the provided dice that satisfies the filters
        // Math.toIntExact because count returns a long
        return Math.toIntExact(this.diceSupplier.get(context).stream()
                .filter(die -> (
                        this.filterShade == 0 || die.getShade().equals(this.filterShade)) &&
                        (this.filterColor == null || die.getColor().equals(this.filterColor))
                ).count());
    }
}
