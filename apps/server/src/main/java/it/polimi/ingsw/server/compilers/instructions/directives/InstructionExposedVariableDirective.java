package it.polimi.ingsw.server.compilers.instructions.directives;

import java.io.Serializable;

/**
 * Defines the directives needed to expose a variable in an instruction.
 */
public class InstructionExposedVariableDirective implements Serializable {

    private static final long serialVersionUID = -8235249115874312908L;

    /**
     * The formal name of the exposed variable.
     */
    private String name;

    /**
     * The class of the exposed variable.
     */
    private Class<? extends Serializable> variableType;

    /**
     * @return the formal name of the exposed variable
     */
    public String getName() {
        return name;
    }

    /**
     * @return the class of the exposed variable
     */
    public Class<? extends Serializable> getVariableType() {
        return variableType;
    }

    /**
     * @param name the formal name of the exposed variable
     * @param variableType the class of the exposed variable
     */
    public InstructionExposedVariableDirective(String name, Class<? extends Serializable> variableType) {
        this.name = name;
        this.variableType = variableType;
    }
}
