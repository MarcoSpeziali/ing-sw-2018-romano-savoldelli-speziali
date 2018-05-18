package it.polimi.ingsw.server.compilers.instructions.predicates;

public class UnrecognizedPredicateException extends RuntimeException {
    private static final long serialVersionUID = 2463649111144879931L;

    public UnrecognizedPredicateException(String predicateId) {
        super("Unrecognized predicate " + predicateId);
    }
}
