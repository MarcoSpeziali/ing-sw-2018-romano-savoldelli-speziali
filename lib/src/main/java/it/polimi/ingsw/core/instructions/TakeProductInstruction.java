package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;

public class TakeProductInstruction extends Instruction {
    @Override
    public Integer run(Context context) {
        return this.instructions.stream()
                .mapToInt(instructions -> instructions.run(context))
                .reduce(0, (result, instructionResult) -> result * instructionResult);
    }
}