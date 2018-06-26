package it.polimi.ingsw.server;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.net.mocks.IEffect;
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

    private static final long serialVersionUID = 3141732749424677799L;

    /**
     * The localized description of the effect.
     */
    private LocalizedString description;

    /**
     * The initial cost of the effect.
     */
    private int initialCost;

    private boolean usedOnce;

    /**
     * The {@link EvaluableConstraint} that evaluates if the effect can be run or not.
     */
    private EvaluableConstraint effectConstraint;

    /**
     * The actions to be run.
     */
    private List<ExecutableAction> actions;

    /**
     * Creates a new instance of {@link Effect}.
     *
     * @param descriptionKey   The localization key for the description of the effect.
     * @param effectConstraint The {@link EvaluableConstraint} that evaluates if the effect can be run or not.
     * @param actions          The actions to be run.
     * @param initialCost      the initial cost of the effect
     */
    public Effect(String descriptionKey, EvaluableConstraint effectConstraint, List<ExecutableAction> actions, int initialCost) {
        this.description = new LocalizedString(descriptionKey);
        this.effectConstraint = effectConstraint;
        this.actions = actions;
        this.initialCost = initialCost;
    }

    /**
     * Creates a new instance of {@link Effect}.
     *
     * @param descriptionKey   The localization key for the description of the effect.
     * @param effectConstraint The {@link EvaluableConstraint} that evaluates if the effect can be run or not.
     * @param actions          The actions to be run.
     */
    public Effect(String descriptionKey, EvaluableConstraint effectConstraint, List<ExecutableAction> actions) {
        this.description = new LocalizedString(descriptionKey);
        this.effectConstraint = effectConstraint;
        this.actions = actions;
        this.initialCost = 1;
    }

    /**
     * @return The localized description of the effect.
     */
    public String getDescription() {
        return description.toString();
    }

    @Override
    public String getDescriptionKey() {
        return this.description.getLocalizationKey();
    }

    /**
     * @return the initial cost of the effect
     */
    public int getInitialCost() {
        return initialCost;
    }

    /**
     * @param initialCost the initial cost of the effect
     */
    public void setInitialCost(int initialCost) {
        this.initialCost = initialCost;
    }

    @Override
    public int getCost() {
        return this.initialCost + (this.usedOnce ? 1 : 0);
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
     * Runs the effect if the {@link EvaluableConstraint} is satisfied.
     *
     * @param cardId the id of the card (used to create a snapshot)
     * @param playerContext the context of the player running the effect
     */
    public void run(String cardId, Context playerContext) {
        this.usedOnce = true;

        if (this.effectConstraint != null && this.effectConstraint.evaluate(Context.getSharedInstance())) {
            throw new ConstraintEvaluationException();
        }
    
        playerContext.snapshot(
                "Effect(" + cardId + ")",
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
    
    /**
     * Runs the effect if the {@link EvaluableConstraint} is satisfied.
     *
     * @param cardId the id of the card (used to create a snapshot)
     */
    public void run(String cardId) {
        this.run(cardId, Context.getSharedInstance());
    }
}
