package it.polimi.ingsw.server.compilers.commons.directives;

import java.io.Serializable;

/**
 * Defines the directives for instantiating a parameter.
 */
public class ParameterDirective implements Serializable {

    private static final long serialVersionUID = 774183294375277756L;

    /**
     * The class of the parameter.
     */
    protected Class<? extends Serializable> parameterType;

    /**
     * The position of the parameter in the constructor.
     */
    protected Integer position;

    /**
     * The name of the parameter (present only if the parameter is optional).
     */
    protected String name;

    /**
     * The default value of the parameter.
     */
    protected Serializable defaultValue;

    /**
     * {@code true} if the parameter class is an array, {@code false} otherwise.
     */
    protected Boolean isMultiple;

    /**
     * @return the class of the parameter
     */
    public Class<? extends Serializable> getParameterType() {
        return parameterType;
    }

    /**
     * @return the position of the parameter in the constructor
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * @return the name of the parameter if {@link #isOptional()} returns {@code true} otherwise {@code null}
     */
    public String getName() {
        return name;
    }

    /**
     * @return the default value of the parameter
     */
    public Serializable getDefaultValue() {
        return defaultValue;
    }

    /**
     * @return {@code true} if the parameter class is an array, {@code false} otherwise
     */
    public Boolean isMultiple() {
        return isMultiple;
    }

    /**
     * @return {@code true} if the parameter is optional, {@code false} otherwise
     */
    public boolean isOptional() {
        return this.name != null;
    }

    /**
     * Instantiate a non-optional parameter with its directives.
     * @param parameterType the class of the parameter
     * @param position the position of the parameter in the constructor
     * @param isMultiple {@code true} if the parameter class is an array, {@code false} otherwise
     */
    public ParameterDirective(Class<? extends Serializable> parameterType, Integer position, Boolean isMultiple) {
        this(parameterType, position, null, null, isMultiple);
    }

    /**
     * Instantiate an optional parameter with its directives.
     * @param parameterType the class of the parameter
     * @param position the position of the parameter in the constructor
     * @param name the name of the parameter
     * @param defaultValue the default value of the parameter
     * @param isMultiple {@code true} if the parameter class is an array, {@code false} otherwise
     */
    public ParameterDirective(Class<? extends Serializable> parameterType, Integer position, String name, Serializable defaultValue, Boolean isMultiple) {
        this.parameterType = parameterType;
        this.position = position;
        this.name = name;
        this.defaultValue = defaultValue;
        this.isMultiple = isMultiple;
    }
}
