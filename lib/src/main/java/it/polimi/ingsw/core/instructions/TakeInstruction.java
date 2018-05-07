package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;

public class TakeInstruction extends Instruction {
    @Override
    public Integer run(Context context) {
        // The take instruction has no sub-instructions (even if they are declared they get ignored)
        // and returns 1, which means that the objective has been achieved.
        return 1;
    }
}
