package it.polimi.ingsw.server.instructions.predicates;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.server.utils.VariableSupplier;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

public class DistinctColorPredicate extends Predicate {

    private static final long serialVersionUID = 2423828840048012309L;
    /**
     * The dice that has to have a distinct color.
     */
    private final VariableSupplier<Die[]> dice;

    public DistinctColorPredicate(VariableSupplier<Die[]> dice) {
        this.id = "distinct_color";
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
        Die[] contextDice = this.dice.get(context);

        if (Arrays.stream(contextDice).anyMatch(Objects::isNull)) {
            return false;
        }

        HashSet<GlassColor> distinctColor = new HashSet<>();

        return Arrays.stream(contextDice)
                .map(Die::getColor)
                .allMatch(distinctColor::add);
    }
}
