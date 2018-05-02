package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;

import java.util.Random;

public class TakeRandomInstruction extends Instruction {
    @Override
    public Integer run(Context context) {
        Random random = new Random(System.currentTimeMillis());

        int[] results = this.instructions.stream()
                .mapToInt(instructions -> instructions.run(context))
                .toArray();

        return random.ints(1, 0, this.instructions.size())
                .map(i -> results[i])
                .findFirst()
                .orElse(0);
    }
}
