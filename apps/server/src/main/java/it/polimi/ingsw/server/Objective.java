package it.polimi.ingsw.server;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.IObjective;
import it.polimi.ingsw.server.instructions.Instruction;
import it.polimi.ingsw.utils.text.LocalizedString;

import java.util.List;

public class Objective implements IObjective {

    /**
     * The number of points gained for each completions.
     */
    private int pointsPerCompletion;

    /**
     * The description key of the objective.
     */
    private LocalizedString description;

    /**
     * The instructions to execute for calculating the number of completions.
     */
    private List<Instruction> instructions;

    /**
     * @return the number of points gained for each completions
     */
    public int getPointsPerCompletion() {
        return pointsPerCompletion;
    }

    /**
     * @return the description key of the objective
     */
    public LocalizedString getDescription() {
        return description;
    }

    /**
     * @return the instructions to execute for calculating the number of completions
     */
    public List<Instruction> getInstructions() {
        return instructions;
    }

    /**
     * @param pointsPerCompletion the number of points gained for each completions
     * @param description the description key of the objective
     * @param instructions the instructions to execute for calculating the number of completions
     */
    public Objective(int pointsPerCompletion, String description, List<Instruction> instructions) {
        this.pointsPerCompletion = pointsPerCompletion;
        this.description = new LocalizedString(description);
        this.instructions = instructions;
    }

    @Override
    public synchronized int calculatePoints(Context context) {
        Context.Snapshot snapshot = Context.getSharedInstance()
                .snapshot("Objective{" + this.description.getLocalizationKey() + "}");

        Integer result = this.instructions.stream()
                .mapToInt(instruction -> instruction.run(snapshot))
                .sum() * this.pointsPerCompletion;

        snapshot.revert();

        return result;
    }
}
