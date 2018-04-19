package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.constraints.EvaluableConstraint;

public interface ExecutableAction {
    String getId();
    String getNextActionId();
    EvaluableConstraint getActionConstraint();

    Object run(Context context);
}
