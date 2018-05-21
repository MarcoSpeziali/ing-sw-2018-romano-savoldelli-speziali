package it.polimi.ingsw.server.compilers.effects;

import it.polimi.ingsw.server.compilers.actions.CompiledExecutableAction;
import it.polimi.ingsw.server.constraints.EvaluableConstraint;

import java.io.Serializable;
import java.util.List;

public class CompiledEffect implements Serializable {

    private static final long serialVersionUID = 2215377943827736251L;

    /**
     * The description key of the effect.
     */
    private String description;

    /**
     * The initial cost of the effect.
     */
    private Integer initialCost;

    /**
     * The constraint to be evaluated before the effect can be run.
     */
    private EvaluableConstraint evaluableConstraint;

    /**
     * The compiled actions of the effect.
     */
    private List<CompiledExecutableAction> compiledExecutableActions;

    /**
     * @return the description key of the effect
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the initial cost of the effect
     */
    public Integer getInitialCost() {
        return initialCost;
    }

    /**
     * @return the constraint to be evaluated before the effect can be run
     */
    public EvaluableConstraint getEvaluableConstraint() {
        return evaluableConstraint;
    }

    /**
     * @return the compiled actions of the effect
     */
    public List<CompiledExecutableAction> getCompiledExecutableActions() {
        return compiledExecutableActions;
    }

    /**
     * @param description the description key of the effect
     * @param initialCost the initial cost of the effect
     * @param evaluableConstraint the constraint to be evaluated before the effect can be run
     * @param compiledExecutableActions the compiled actions of the effect
     */
    public CompiledEffect(String description, Integer initialCost, EvaluableConstraint evaluableConstraint, List<CompiledExecutableAction> compiledExecutableActions) {
        this.description = description;
        this.initialCost = initialCost;
        this.evaluableConstraint = evaluableConstraint;
        this.compiledExecutableActions = compiledExecutableActions;
    }
}
