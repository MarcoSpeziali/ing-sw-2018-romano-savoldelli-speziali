package it.polimi.ingsw.server;

import it.polimi.ingsw.core.IEffect;
import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.ToolCard;
import it.polimi.ingsw.server.actions.ExecutableAction;
import it.polimi.ingsw.server.constraints.ConstraintEvaluationException;
import it.polimi.ingsw.server.constraints.EvaluableConstraint;
import it.polimi.ingsw.utils.text.LocalizedString;

import java.util.List;

/**
 * Represents an effect associated with a {@link ToolCard}.
 */
public class Effect implements IEffect {

    /**
     * The localized description of the effect.
     */
    private LocalizedString description;

    /**
     * The {@link EvaluableConstraint} that evaluates if the effect can be run or not.
     */
    private EvaluableConstraint effectConstraint;

    /**
     * The actions to be run.
     */
    private List<ExecutableAction> actions;

    /**
     * @return The localized description of the effect.
     */
    public String getDescription() {
        return description.toString();
    }

    /**
     * @return The {@link EvaluableConstraint} that evaluates if the effect can be run or not.
     */
    public EvaluableConstraint getEffectConstraint() {
        return effectConstraint;
    }

    /**
     * @return The actions to be run.
     */
    public List<ExecutableAction> getActions() {
        return actions;
    }

    /**
     * Creates a new instance of {@link Effect}.
     * @param descriptionKey The localization key for the description of the effect.
     * @param effectConstraint The {@link EvaluableConstraint} that evaluates if the effect can be run or not.
     * @param actions The actions to be run.
     */
    public Effect(String descriptionKey, EvaluableConstraint effectConstraint, List<ExecutableAction> actions) {
        this.description = new LocalizedString(descriptionKey);
        this.effectConstraint = effectConstraint;
        this.actions = actions;
    }

    /**
     * Runs the effect if the {@link EvaluableConstraint} is satisfied.
     * @param cardId The id of the card (used to create a snapshot).
     */
    @Override
    public void run(String cardId) {
        if (this.effectConstraint != null && this.effectConstraint.evaluate(Context.getSharedInstance())) {
            throw new ConstraintEvaluationException();
        }

        Context.getSharedInstance().snapshot(
                "Effect{" + cardId + "}",
                snapshot -> this.actions.forEach(action -> {
                    if (action.getActionData().getResultIdentifier() == null) {
                        action.run(snapshot);
                    }
                    else {
                        snapshot.put(action.getActionData().getResultIdentifier(), action.run(snapshot));
                    }
                })
        );
    }
}
