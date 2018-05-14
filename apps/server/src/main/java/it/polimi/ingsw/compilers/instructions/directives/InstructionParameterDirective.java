package it.polimi.ingsw.compilers.instructions.directives;

import it.polimi.ingsw.compilers.commons.directives.ParameterDirective;

import java.io.Serializable;

/**
 * Defines the directives for instantiating a parameter of an instruction.
 */
public class InstructionParameterDirective extends ParameterDirective {

    private static final long serialVersionUID = -6713331443174816615L;

    /**
     * {@code true} if the parameter class is a predicate, {@code false} otherwise.
     */
    private Boolean isPredicate;

    /**
     * @return {@code true} if the parameter class is a predicate, {@code false} otherwise
     */
    public Boolean isPredicate() {
        return isPredicate;
    }

    /**
     * If the parameter is optional.
     */
    private Boolean isOptional;

    @Override
    public boolean isOptional() {
        return this.isOptional;
    }

    /**
     * Instantiate a non-optional parameter with its directives.
     *
     * @param parameterType the class of the parameter
     * @param position      the position of the parameter in the constructor
     * @param isMultiple    {@code true} if the parameter class is an array, {@code false} otherwise
     */
    public InstructionParameterDirective(Class<? extends Serializable> parameterType, Integer position, Boolean isMultiple, Boolean isPredicate, Boolean isOptional) {
        this(parameterType, position, null, null, isMultiple, isPredicate, isOptional);
    }

    /**
     * Instantiate an optional parameter with its directives.
     *
     * @param parameterType the class of the parameter
     * @param position      the position of the parameter in the constructor
     * @param name          the name of the parameter
     * @param defaultValue  the default value of the parameter
     * @param isMultiple    {@code true} if the parameter class is an array, {@code false} otherwise
     */
    public InstructionParameterDirective(Class<? extends Serializable> parameterType, Integer position, String name, Serializable defaultValue, Boolean isMultiple, Boolean isPredicate, Boolean isOptional) {
        super(parameterType, position, name, defaultValue, isMultiple);
        this.isPredicate = isPredicate;
        this.isOptional = isOptional;
    }
}
