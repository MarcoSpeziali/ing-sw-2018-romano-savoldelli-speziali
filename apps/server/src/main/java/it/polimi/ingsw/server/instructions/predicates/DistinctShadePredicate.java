package it.polimi.ingsw.server.instructions.predicates;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.server.utils.VariableSupplier;

import java.util.Arrays;
import java.util.HashSet;

public class DistinctShadePredicate extends Predicate {

    private static final long serialVersionUID = -3020074770083880056L;
    /**
     * The dice that has to have a distinct shade.
     */
    private final VariableSupplier<Die[]> dice;

    public DistinctShadePredicate(VariableSupplier<Die[]> dice) {
        this.id = "distinct_shade";
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
                .map(Die::getShade)
                .allMatch(new HashSet<>()::add);
    }
}
