package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.constraints.EvaluableConstraint;

import java.io.Serializable;

/**
 * Holds the common data for an {@code ExecutableAction}.
 */
public class ActionData implements Serializable {

    private static final long serialVersionUID = 6312073718156081520L;

    /**
     * The key of the string that describes the action.
     */
    private final String descriptionKey;

    /**
     * The constraint of the action.
     */
    @SuppressWarnings("squid:S1948")
    private final EvaluableConstraint constraint;

    /**
     * The name of the result, used by the next actions.
     */
    private final String resultIdentifier;

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
     * @return The name of the result, used by the next actions.
     */
    public String getResultIdentifier() {
        return resultIdentifier;
    }

    /**
     * Creates a new instance of {@code ActionData}.
     * @param constraint The constraint of the action.
     * @param resultIdentifier The name of the result, used by the next actions.
     */
    public ActionData(String descriptionKey, EvaluableConstraint constraint, String resultIdentifier) {
        this.descriptionKey = descriptionKey;
        this.constraint = constraint;
        this.resultIdentifier = resultIdentifier;
    }
}
