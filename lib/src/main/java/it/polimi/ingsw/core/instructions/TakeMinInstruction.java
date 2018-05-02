package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;

public class TakeMinInstruction extends Instruction {
    @Override
    public Integer run(Context context) {
        return this.instructions.stream()
                .mapToInt(instructions -> instructions.run(context))
                .min()
                .orElse(Integer.MAX_VALUE);
    }
}
