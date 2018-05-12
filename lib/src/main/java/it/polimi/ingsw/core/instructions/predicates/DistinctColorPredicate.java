package it.polimi.ingsw.core.instructions.predicates;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.actions.VariableSupplier;
import it.polimi.ingsw.models.Die;

import java.util.Arrays;
import java.util.HashSet;

public class DistinctColorPredicate extends Predicate {
    /**
     * The dice that has to have a distinct color.
     */
    private final VariableSupplier<Die[]> dice;

    public DistinctColorPredicate(VariableSupplier<Die[]> dice) {
        this.dice = dice;
    }

    /**
     * Evaluates the predicate.
     *
     * @param context an instance of {@link Context}
     * @return {@code true} if the predicate is respected, {@code false} otherwise
     */
    @Override
    public boolean evaluate(Context context) {
        return Arrays.stream(this.dice.get(context))
                .map(Die::getColor)
                .allMatch(new HashSet<>()::add);
    }
}
