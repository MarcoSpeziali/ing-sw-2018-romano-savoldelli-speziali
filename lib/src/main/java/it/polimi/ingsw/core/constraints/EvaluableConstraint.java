package it.polimi.ingsw.core.constraints;

import it.polimi.ingsw.core.Context;

@FunctionalInterface
public interface EvaluableConstraint {
    /**
     * @param context The context on which the constraints will be ran.
     * @return {@code True} if the constraint is satisfied, {@code false} otherwise.
     */
    boolean evaluate(Context context);
}
