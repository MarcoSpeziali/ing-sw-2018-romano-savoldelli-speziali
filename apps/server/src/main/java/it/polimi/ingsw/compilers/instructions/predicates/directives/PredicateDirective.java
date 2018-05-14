package it.polimi.ingsw.compilers.instructions.predicates.directives;

import it.polimi.ingsw.compilers.commons.directives.ClassDirective;
import it.polimi.ingsw.compilers.commons.directives.ParameterDirective;
import it.polimi.ingsw.core.instructions.predicates.Predicate;

import java.io.Serializable;
import java.util.List;

public class PredicateDirective extends ClassDirective<Predicate> implements Serializable {

    private static final long serialVersionUID = -6631411764424983597L;

    /**
     * @param id                   the id of the predicate
     * @param targetClass          the class to instantiate
     * @param parametersDirectives the directives to instantiate the parameters
     */
    public PredicateDirective(String id, Class<? extends Predicate> targetClass, List<ParameterDirective> parametersDirectives) {
        super(id, targetClass, parametersDirectives);
    }
}
