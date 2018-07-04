package it.polimi.ingsw.server;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.net.mocks.IObjective;
import it.polimi.ingsw.server.instructions.Instruction;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

import java.util.List;

public class Objective implements IObjective {

    private static final long serialVersionUID = -6454176762990817582L;

    /**
     * The number of points gained for each completions.
     */
    private int pointsPerCompletion;

    /**
     * The description key of the objective.
     */
    private String description;

    /**
     * The instructions to execute for calculating the number of completions.
     */
    private transient List<Instruction> instructions;

    /**
     * @param pointsPerCompletion the number of points gained for each completions
     * @param description         the description key of the objective
     * @param instructions        the instructions to execute for calculating the number of completions
     */
    public Objective(int pointsPerCompletion, String description, List<Instruction> instructions) {
        this.pointsPerCompletion = pointsPerCompletion;
        this.description = description;
        this.instructions = instructions;
    }

    @JSONDesignatedConstructor
    Objective(@JSONElement("points-per-completion") int pointsPerCompletion) {
        this.pointsPerCompletion = pointsPerCompletion;
    }

    /**
     * @return the number of points gained for each completions
     */
    @JSONElement("points-per-completion")
    public int getPointsPerCompletion() {
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
    public List<Instruction> getInstructions() {
        return instructions;
    }

    public int calculatePoints(Context context) {
        Context.Snapshot snapshot = context
                .snapshot("Objective(" + this.description + ")");

        Integer result = this.instructions.stream()
                .mapToInt(instruction -> instruction.run(snapshot))
                .sum() * this.pointsPerCompletion;

        snapshot.revert();

        return result;
    }
}
