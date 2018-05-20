package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.utils.IterableRange;

import java.util.Map;

public class ForRowInstruction extends Instruction {
    /**
     * The name for the exposed variable mapping.
     */
    protected static final String ROW_VARIABLE_NAME = "row";

    /**
     * Maps the user-defined variable to the variables this class exposes.
     */
    protected Map<String, String> exposedVariableMapping;

    /**
     * @param exposedVariableMapping the user-defined names for the exposed variables
     */
    public ForRowInstruction(Map<String, String> exposedVariableMapping) {
        this.exposedVariableMapping = exposedVariableMapping;
    }

    @Override
    public Integer run(Context context) {
        // Gets the user-defined name for the exposed variable 'column'
        String exposedName = exposedVariableMapping.get(ROW_VARIABLE_NAME);

        // Creates a snapshot for the sub-instructions
        Context.Snapshot snapshot = context.snapshot("for-row(" + exposedName + ")");

        // Retrieves the window from the context
        Window window = (Window) context.get(Context.WINDOW);

        // The results is the sum of each result of the iteration
        Integer result = new IterableRange<>(
                0,
                window.getCells().length - 1,
                IterableRange.INTEGER_INCREMENT_FUNCTION
        ).stream().mapToInt(index -> {
                    // The exposed variable gets put inside the snapshot
                    snapshot.put(exposedName, getDiceInRowFromWindow(window, index));

                    // Returns the result of the sub-instructions
                    return super.run(snapshot);
                }).sum();

        // Reverts the context to its original state
        snapshot.revert();

        return result;
    }

    /**
     * Returns the dice in the row at {@code index}.
     * @param window The window holding the dice.
     * @param index The index of the row containing the wanted dice.
     * @return The dice in the row at {@code index}.
     */
    private static Die[] getDiceInRowFromWindow(Window window, int index) {
        Die[] rowDice = new Die[window.getCells()[index].length];

        for (int i = 0; i < rowDice.length; i++) {
            rowDice[i] = window.getCells()[index][i].getDie();
        }

        return rowDice;
    }
}
