package it.polimi.ingsw.core.instructions.predicates;

import it.polimi.ingsw.core.Context;

/**
 * Represents a boolean check.
 */
public abstract class Predicate {

    /**
     * The id of the predicate.
     */
    protected String id;

    /**
     * @return the id of the predicate
     */
    public String getId() {
        return this.id;
    }

    /**
     * Evaluates the predicate.
     * @param context an instance of {@link Context}
     * @return {@code true} if the predicate is respected, {@code false} otherwise
     */
    public abstract boolean evaluate(Context context);
}
