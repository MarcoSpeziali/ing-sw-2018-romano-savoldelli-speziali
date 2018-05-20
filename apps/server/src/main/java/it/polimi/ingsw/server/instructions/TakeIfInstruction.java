package it.polimi.ingsw.server.instructions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.server.instructions.predicates.Predicate;

import java.util.Map;

public class TakeIfInstruction extends Instruction {

    private static final String IF_PREDICATE_NAME = "predicate";

    /**
     * The predicate to evaluate.
     */
    private final Predicate predicate;

    public TakeIfInstruction(Map<String, Predicate> predicateMap) {
        this.predicate = predicateMap.get(IF_PREDICATE_NAME);
    }

    @Override
    public Integer run(Context context) {
        // The take instruction has no sub-instructions (even if they are declared they get ignored)
        // and returns 1 if the predicate returns true, 0 otherwise
        return this.predicate.evaluate(context) ? 1 : 0;
    }
}
