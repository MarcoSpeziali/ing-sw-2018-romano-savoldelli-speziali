package it.polimi.ingsw.core.constraints;

import it.polimi.ingsw.core.Context;

@FunctionalInterface
public interface EvaluableConstraint {
    boolean evaluate(Context context);
}
