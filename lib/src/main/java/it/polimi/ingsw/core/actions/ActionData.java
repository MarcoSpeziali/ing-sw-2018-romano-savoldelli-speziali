package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.constraints.ConstraintGroup;
import it.polimi.ingsw.core.constraints.EvaluableConstraint;

/**
 * Holds the common data for an ExecutableAction.
 */
public class ActionData {

    /**
     * The id of the action.
     */
    private final String id;

    /**
     * The id of the next action.
     */
    private final String nextActionId;

    /**
     * The constraint of the action.
     */
    private final EvaluableConstraint constraint;

    /**
     * @return The id of the action.
     */
    public String getId() {
        return this.id;
    }

    /**
     * @return The id of the next action.
     */
    public String getNextActionId() {
        return this.nextActionId;
    }

    /**
     * @return The constraint of the action.
     */
    public EvaluableConstraint getConstraint() {
        return this.constraint;
    }

    /**
     * Creates a new instance of `ActionData`.
     * @param id The id of the action.
     * @param nextActionId The id of the next action.
     * @param constraint The constraint of the action.
     */
    public ActionData(String id, String nextActionId, ConstraintGroup constraint) {
        this.id = id;
        this.nextActionId = nextActionId;
        this.constraint = constraint;
    }
}
