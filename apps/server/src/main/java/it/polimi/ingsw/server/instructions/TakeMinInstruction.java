package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;

public class TakeMinInstruction extends Instruction {
    @Override
    public Integer run(Context context) {
        // Selects the minimum value among the results of each sub-instruction
        return this.instructions.stream()
                .mapToInt(instructions -> instructions.run(context))
                .min()
                .orElse(Integer.MAX_VALUE);
    }
}
