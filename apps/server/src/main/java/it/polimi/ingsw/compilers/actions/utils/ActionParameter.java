package it.polimi.ingsw.compilers.actions.utils;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.actions.Action;
import it.polimi.ingsw.core.actions.VariableSupplier;

import java.io.Serializable;

/**
 * Represents a parameter for the function call {@link Action#run(Context)}.
 */
public class ActionParameter implements Serializable {
    private static final long serialVersionUID = -6487671097074774515L;

	/**
     * Represents the class of the parameter.
     */
    private Class<? extends Serializable> type;

    /**
     * Represents the position of the parameter in the function call.
     */
    private Integer position;

    /**
     * Represents a wrapper for the parameter value.
     * 
     * This because the parameter could need a computation before being usable.
     */
    @SuppressWarnings("squid:S1948")
    private VariableSupplier<? extends Serializable> parameterValue;

    /**
     * The name of the optional parameter that this parameter overrides (if any).
     */
    private String optionalName;

    /**
     * {@code true} if the parameter is optional.
     */
    private Boolean isOptional;

    /**
     * The default value of the parameter.
     */
    private Serializable defaultValue;

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

    /**
     * @param type the class of the parameter
     * @param position the position of the parameter in the function call
     * @param parameterValue the wrapper for the parameter value
     * @param optionalName the name of the optional parameter that this parameter overrides (if any)
     * @param defaultValue the default value of the parameter
     */
    public ActionParameter(Class<? extends Serializable> type, Integer position, VariableSupplier<? extends Serializable> parameterValue, String optionalName, Serializable defaultValue) {
        this.type = type;
        this.position = position;
        this.parameterValue = parameterValue;
        this.optionalName = optionalName;
        this.isOptional = optionalName != null;
        this.defaultValue = defaultValue;
    }
}