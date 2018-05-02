package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;

import java.util.List;

/**
 * Represents a single instruction that can be executed and results in a partial score.
 */
public abstract class Instruction {
    /**
     * An ordered list of instructions which represents the inner instruction to execute.
     */
    protected List<Instruction> instructions;

    /**
     * @return An ordered list of instructions which represents the inner instruction to execute.
     */
    public List<Instruction> getInstructions() {
        return this.instructions;
    }

    /**
     * Runs the instruction.
     * @param context The context that holds the variables needed by the instruction.
     * @return The number of times that the objective has been achieved.
     */
    public Integer run(Context context) {
        return this.instructions.stream()
                .mapToInt(instruction -> instruction.run(context))
                .sum();
    }
}
