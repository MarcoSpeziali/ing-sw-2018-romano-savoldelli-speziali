package it.polimi.ingsw.server.constraints;

import it.polimi.ingsw.core.Context;

import java.io.Serializable;

//@FunctionalInterface
public interface EvaluableConstraint extends Serializable {
    /**
     * @param context The context on which the constraints will be ran.
     * @return {@code True} if the constraint is satisfied, {@code false} otherwise.
     */
    boolean evaluate(Context context);

    /**
     * @return The id of the constraint.
     */
    String getId();
}
