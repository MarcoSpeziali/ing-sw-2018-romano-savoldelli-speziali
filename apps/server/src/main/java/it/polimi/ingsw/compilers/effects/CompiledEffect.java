package it.polimi.ingsw.compilers.effects;

import it.polimi.ingsw.compilers.actions.CompiledExecutableAction;

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
     * @return the compiled actions of the effect
     */
    public List<CompiledExecutableAction> getCompiledExecutableActions() {
        return compiledExecutableActions;
    }

    /**
     * @param description the description key of the effect
     * @param initialCost the initial cost of the effect
     * @param compiledExecutableActions the compiled actions of the effect
     */
    public CompiledEffect(String description, Integer initialCost, List<CompiledExecutableAction> compiledExecutableActions) {
        this.description = description;
        this.initialCost = initialCost;
        this.compiledExecutableActions = compiledExecutableActions;
    }
}
