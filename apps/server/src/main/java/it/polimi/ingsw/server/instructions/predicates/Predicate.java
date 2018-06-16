package it.polimi.ingsw.server.instructions.predicates;

import it.polimi.ingsw.core.Context;

import java.io.Serializable;

/**
 * Represents a boolean check.
 */
public abstract class Predicate implements Serializable {

    private static final long serialVersionUID = -8125598992789477226L;

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
     *
     * @param context an instance of {@link Context}
     * @return {@code true} if the predicate is respected, {@code false} otherwise
     */
    public abstract boolean evaluate(Context context);
}
