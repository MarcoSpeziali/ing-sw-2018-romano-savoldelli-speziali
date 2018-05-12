package it.polimi.ingsw.compilers.instructions.predicates;

import it.polimi.ingsw.compilers.commons.CompiledParameter;
import it.polimi.ingsw.core.instructions.predicates.Predicate;

import java.io.Serializable;
import java.util.List;

public class CompiledPredicate implements Serializable {

    private static final long serialVersionUID = 7723007452831540204L;

    /**
     * The id of the predicate.
     */
    private String predicateId;

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
    public String getPredicateId() {
        return predicateId;
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
     * @param predicateId the id of the predicate
     * @param predicateClass the class of the predicate
     * @param parameters the parameters of the predicate
     */
    public CompiledPredicate(String predicateId, Class<? extends Predicate> predicateClass, List<CompiledParameter> parameters) {
        this.predicateId = predicateId;
        this.predicateClass = predicateClass;
        this.parameters = parameters;
    }
}
