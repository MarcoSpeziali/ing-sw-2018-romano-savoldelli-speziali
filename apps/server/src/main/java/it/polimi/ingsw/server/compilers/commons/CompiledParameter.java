package it.polimi.ingsw.server.compilers.commons;

import it.polimi.ingsw.server.utils.VariableSupplier;

import java.io.Serializable;

public class CompiledParameter implements Serializable {

    private static final long serialVersionUID = -7548383616951562022L;

    /**
     * Represents the class of the parameter.
     */
    protected Class<? extends Serializable> type;

    /**
     * Represents the position of the parameter in the function call.
     */
    protected Integer position;

    /**
     * Represents a wrapper for the parameter value.
     * <p>
     * This because the parameter could need a computation before being usable.
     */
    @SuppressWarnings("squid:S1948")
    protected VariableSupplier<? extends Serializable> parameterValue;

    /**
     * The name of the optional parameter that this parameter overrides (if any).
     */
    protected String optionalName;

    /**
     * {@code true} if the parameter is optional.
     */
    protected Boolean isOptional;

    /**
     * The default value of the parameter.
     */
    protected Serializable defaultValue;

    /**
     * @param type           the class of the parameter
     * @param position       the position of the parameter in the function call
     * @param parameterValue the wrapper for the parameter value
     * @param optionalName   the name of the optional parameter that this parameter overrides (if any)
     * @param defaultValue   the default value of the parameter
     */
    public CompiledParameter(Class<? extends Serializable> type, Integer position, VariableSupplier<? extends Serializable> parameterValue, String optionalName, Serializable defaultValue) {
        this.type = type;
        this.position = position;
        this.parameterValue = parameterValue;
        this.optionalName = optionalName;
        this.isOptional = optionalName != null;
        this.defaultValue = defaultValue;
    }

    /**
     * @return the class of the parameter
     */
    @SuppressWarnings("squid:S1452")
    public Class<? extends Serializable> getType() {
        return type;
    }

    /**
     * @return the position of the parameter in the function call
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * @return the wrapper for the parameter value.
     */
    @SuppressWarnings("squid:S1452")
    public VariableSupplier<? extends Serializable> getParameterValue() {
        return parameterValue;
    }

    /**
     * @return the name of the optional parameter that this parameter overrides (if any)
     */
    public String getOptionalName() {
        return optionalName;
    }

    /**
     * @return {@code true} if the parameter is optional
     */
    public Boolean isOptional() {
        return isOptional;
    }

    /**
     * @return the default value of the parameter
     */
    public Serializable getDefaultValue() {
        return defaultValue;
    }
}
