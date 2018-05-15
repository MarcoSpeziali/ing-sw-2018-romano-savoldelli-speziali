package it.polimi.ingsw.compilers.objectives;

import it.polimi.ingsw.compilers.instructions.CompiledInstruction;

import java.io.Serializable;
import java.util.List;

public class CompiledObjective implements Serializable {

    private static final long serialVersionUID = 8057251807886255401L;

    /**
     * The number of point gained for each completions.
     */
    private Integer pointsPerCompletion;

    /**
     * The description key of the objective.
     */
    private String description;

    /**
     * The instructions to execute for calculating the number of completions.
     */
    private List<CompiledInstruction> instructions;

    /**
     * @return the number of point gained for each completions
     */
    public Integer getPointsPerCompletion() {
        return pointsPerCompletion;
    }

    /**
     * @return the description key of the objective
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the instructions to execute for calculating the number of completions
     */
    public List<CompiledInstruction> getInstructions() {
        return instructions;
    }

    /**
     * @param pointsPerCompletion the number of point gained for each completions
     * @param description the description key of the objective
     * @param instructions the instructions to execute for calculating the number of completions
     */
    public CompiledObjective(String description, Integer pointsPerCompletion, List<CompiledInstruction> instructions) {
        this.pointsPerCompletion = pointsPerCompletion;
        this.description = description;
        this.instructions = instructions;
    }
}
