package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Window;

import java.util.Map;

public class ForDieInstruction extends Instruction {
    /**
     * The name for the exposed variable mapping.
     */
    protected static final String DIE_VARIABLE_NAME = "die";

    private final Integer filterShade;
    private final GlassColor filterColor;

    /**
     * Maps the user-defined variable to the variables this class exposes.
     */
    protected Map<String, String> exposedVariableMapping;

    /**
     * @param exposedVariableMapping the user-defined names for the exposed variables
     * @param filterShade the shade to filter
     * @param filterColor the color to filter
     */
    public ForDieInstruction(Map<String, String> exposedVariableMapping, Integer filterShade, GlassColor filterColor) {
        this.filterShade = filterShade;
        this.filterColor = filterColor;

        this.exposedVariableMapping = exposedVariableMapping;
    }

    @Override
    public Integer run(Context context) {
        // Gets the user-defined name for the exposed variable 'die'
        String exposedName = exposedVariableMapping.get(DIE_VARIABLE_NAME);

        // Creates a snapshot for the sub-instructions
        Context.Snapshot snapshot = context.snapshot("for-die(" + exposedName + ")");

        // Retrieves the window from the context
        Window window = (Window) context.get(Context.WINDOW);

        // The results is the sum of each result of the iteration over each die
        Integer result = window.getDice().stream()
                // The dice collection gets filtered with the provided filters
                .filter(die -> (
                        this.filterShade == 0 || die.getShade().equals(this.filterShade)) &&
                        (this.filterColor == null || die.getColor().equals(this.filterColor))
                ).mapToInt(die -> {
                    // The exposed variables get put inside the snapshot
                    snapshot.put(exposedName, die);

                    // Returns the result of the sub-instructions
                    return super.run(snapshot);
                }).sum();

        // Reverts the context to its original state
        snapshot.revert();

        return result;
    }
}
