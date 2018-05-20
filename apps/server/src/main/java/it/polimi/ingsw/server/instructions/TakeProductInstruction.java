package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;

public class TakeProductInstruction extends Instruction {
    @Override
    public Integer run(Context context) {
        // Selects the product of the results of each sub-instruction
        return this.instructions.stream()
                .mapToInt(instructions -> instructions.run(context))
                .reduce((result, instructionResult) -> result * instructionResult)
                .orElse(0);
    }
}