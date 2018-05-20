package it.polimi.ingsw.server.compilers.instructions.predicates;

import it.polimi.ingsw.server.compilers.commons.CompiledParameter;
import it.polimi.ingsw.core.instructions.predicates.Predicate;

import java.io.Serializable;
import java.util.List;

public class CompiledPredicate implements Serializable {

    private static final long serialVersionUID = 7723007452831540204L;

    /**
     * The name of the predicate.
     */
    private String predicateName;

    /**
     * The class of the predicate.
     */
    private Class<? extends Predicate> predicateClass;

    /**
     * The parameters of the predicate.
     */
    private List<CompiledParameter> parameters;

    /**
     * @return the id of the predicate
     */
    public String getPredicateName() {
        return this.predicateName;
    }

    /**
     * @return the class of the predicate
     */
    public Class<? extends Predicate> getPredicateClass() {
        return predicateClass;
    }

    /**
     * @return the parameters of the predicate
     */
    public List<CompiledParameter> getParameters() {
        return parameters;
    }

    /**
     * @param predicateName the id of the predicate
     * @param predicateClass the class of the predicate
     * @param parameters the parameters of the predicate
     */
    public CompiledPredicate(String predicateName, Class<? extends Predicate> predicateClass, List<CompiledParameter> parameters) {
        this.predicateName = predicateName;
        this.predicateClass = predicateClass;
        this.parameters = parameters;
    }
}
