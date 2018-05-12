package it.polimi.ingsw.compilers.instructions.predicates.directives;

import it.polimi.ingsw.compilers.commons.directives.ParameterDirective;
import it.polimi.ingsw.core.instructions.predicates.Predicate;

import java.io.Serializable;
import java.util.List;

public class PredicateDirective implements Serializable {
    private static final long serialVersionUID = -6631411764424983597L;

    /**
     * The id of the predicate.
     */
    private String id;

    /**
     * The class to instantiate to execute the predicate.
     */
    private Class<? extends Predicate> targetClass;

    /**
     * The directives to instantiate the constructor parameters.
     */
    private List<ParameterDirective> parametersDirectives;

    /**
     * @return the id of the predicate
     */
    public String getId() {
        return id;
    }

    /**
     * @return the class to instantiate to execute the predicate
     */
    public Class<? extends Predicate> getTargetClass() {
        return targetClass;
    }

    /**
     * @return the directives to instantiate the parameters
     */
    public List<ParameterDirective> getParametersDirectives() {
        return parametersDirectives;
    }

    /**
     * @param id the id of the predicate
     * @param targetClass the class to instantiate to execute the predicate
     * @param parametersDirectives the directives to instantiate the parameters
     */
    public PredicateDirective(String id, Class<? extends Predicate> targetClass, List<ParameterDirective> parametersDirectives) {
        this.id = id;
        this.targetClass = targetClass;
        this.parametersDirectives = parametersDirectives;
    }
}
