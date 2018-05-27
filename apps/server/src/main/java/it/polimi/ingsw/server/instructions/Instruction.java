package it.polimi.ingsw.server.instructions;

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
     * Gets the instructions.
     * @return An ordered list of instructions which represents the inner instruction to execute.
     */
    public List<Instruction> getInstructions() {
        return this.instructions;
    }

    /**
     * Sets the instructions.
     * @param instructions An ordered list of instructions which represents the inner instruction to execute.
     */
    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    /**
     * Runs the instruction, if no instructions are provided the default value of 0 is returned.
     * @param context The context that holds the variables needed by the instruction.
     * @return The number of times that the objective has been achieved.
     */
    public Integer run(Context context) {
        if (this.instructions != null) {
            if (this.instructions.isEmpty()) {
                return 0;
            }
        }
        else {
            return 0;
        }

        return this.instructions.stream()
                .mapToInt(instruction -> instruction.run(context))
                .sum();
    }
}
