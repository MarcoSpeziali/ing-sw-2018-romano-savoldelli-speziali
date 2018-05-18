package it.polimi.ingsw.server.compilers.instructions;

import java.io.Serializable;

public class CompiledExposedVariable implements Serializable {

    private static final long serialVersionUID = -189322770197284090L;

    /**
     * The default name of the exposed variable.
     */
    private final String defaultName;

    /**
     * The name of the exposed variable that overrides the default name represented by {@link #getDefaultName()}.
     */
    private String overrideValue;

    /**
     * The class of the exposed variable.
     */
    private Class<? extends Serializable> exposedVariableType;

    /**
     * @return the default name of the exposed variable
     */
    public String getDefaultName() {
        return this.defaultName;
    }

    /**
     * @return the name of the variable that overrides the default name represented by {@link #getDefaultName()}
     */
    public String getOverrideValue() {
        return this.overrideValue;
    }

    /**
     * @return the class of the exposed variable
     */
    public Class<? extends Serializable> getExposedVariableType() {
        return exposedVariableType;
    }

    /**
     * @param defaultName the default name of the exposed variable
     * @param overrideValue the name of the variable that overrides the default name represented by {@link #getDefaultName()}
     * @param exposedVariableType the class of the exposed variable
     */
    public CompiledExposedVariable(String defaultName, String overrideValue, Class<? extends Serializable> exposedVariableType) {
        this.defaultName = defaultName;
        this.overrideValue = overrideValue;
        this.exposedVariableType = exposedVariableType;
    }
}
