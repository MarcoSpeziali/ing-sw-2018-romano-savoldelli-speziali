package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.constraints.EvaluableConstraint;

/**
 * Holds the common data for an {@code ExecutableAction}.
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
     * The key of the string that describes the action.
     */
    private final String descriptionKey;

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
     * @return The key of the string that describes the action.
     */
    public String getDescriptionKey() {
        return descriptionKey;
    }

    /**
     * @return The constraint of the action.
     */
    public EvaluableConstraint getConstraint() {
        return this.constraint;
    }

    /**
     * Creates a new instance of {@code ActionData}.
     * @param id The id of the action.
     * @param nextActionId The id of the next action.
     * @param constraint The constraint of the action.
     */
    public ActionData(String id, String nextActionId, String descriptionKey, EvaluableConstraint constraint) {
        this.id = id;
        this.nextActionId = nextActionId;
        this.descriptionKey = descriptionKey;
        this.constraint = constraint;
    }
}
