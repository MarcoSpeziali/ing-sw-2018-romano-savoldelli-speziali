package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;

public class TakeMaxInstruction extends Instruction {
    @Override
    public Integer run(Context context) {
        return this.instructions.stream()
                .mapToInt(instructions -> instructions.run(context))
                .max()
                .orElse(Integer.MIN_VALUE);
    }
}
