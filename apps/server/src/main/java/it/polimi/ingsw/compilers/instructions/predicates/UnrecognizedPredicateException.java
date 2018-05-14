package it.polimi.ingsw.compilers.instructions.predicates;

public class UnrecognizedPredicateException extends RuntimeException {
    public UnrecognizedPredicateException(String predicateId) {
        super("Unrecognized predicate " + predicateId);
    }
}
