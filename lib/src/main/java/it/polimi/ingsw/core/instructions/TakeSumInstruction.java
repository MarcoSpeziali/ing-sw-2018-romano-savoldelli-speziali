package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;

public class TakeSumInstruction extends Instruction {
    @Override
    public Integer run(Context context) {
        // Selects the sum of the results of each sub-instruction
        return this.instructions.stream()
                .mapToInt(instructions -> instructions.run(context))
                .sum();
    }
}
