package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.instructions.predicates.Predicate;

public class TakeIfInstruction extends Instruction {

    /**
     * The predicate to evaluate.
     */
    private final Predicate predicate;

    public TakeIfInstruction(Predicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public Integer run(Context context) {
        // The take instruction has no sub-instructions (even if they are declared they get ignored)
        // and returns 1 if the predicate returns true, 0 otherwise
        return this.predicate.evaluate(context) ? 1 : 0;
    }
}
