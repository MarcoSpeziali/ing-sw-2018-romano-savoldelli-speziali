package it.polimi.ingsw.compilers.actions.directives;

import java.io.Serializable;

/**
 * Defines the directives for instantiating a parameter of an action.
 */
public class ActionParameterDirective implements Serializable {

    private static final long serialVersionUID = 774183294375277756L;

    /**
     * The class of the parameter.
     */
    private Class<? extends Serializable> parameterType;

    /**
     * The position of the parameter in the constructor.
     */
    private Integer position;

    /**
     * The name of the parameter (present only if the parameter is optional).
     */
    private String name;

    /**
     * The default value of the parameter.
     */
    private Serializable defaultValue;


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
     * @return {@code true} if the parameter is optional, {@code false} otherwise
     */
    public boolean isOptional() {
        return this.name != null;
    }

    /**
     * Instantiate a non-optional parameter with its directives.
     * @param parameterType the class of the parameter
     * @param position the position of the parameter in the constructor
     */
    public ActionParameterDirective(Class<? extends Serializable> parameterType, Integer position) {
        this(parameterType, position, null, null);
    }

    /**
     * Instantiate an optional parameter with its directives.
     * @param parameterType the class of the parameter
     * @param position the position of the parameter in the constructor
     * @param name the name of the parameter
     * @param defaultValue the default value of the parameter
     */
    public ActionParameterDirective(Class<? extends Serializable> parameterType, Integer position, String name, Serializable defaultValue) {
        this.parameterType = parameterType;
        this.position = position;
        this.name = name;
        this.defaultValue = defaultValue;
    }
}
