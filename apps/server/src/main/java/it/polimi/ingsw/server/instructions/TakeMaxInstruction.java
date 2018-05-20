package it.polimi.ingsw.server.instructions;

import it.polimi.ingsw.core.Context;

public class TakeMaxInstruction extends Instruction {
    @Override
    public Integer run(Context context) {
        // Selects the maximum value among the results of each sub-instruction
        return this.instructions.stream()
                .mapToInt(instructions -> instructions.run(context))
                .max()
                .orElse(Integer.MIN_VALUE);
    }
}
