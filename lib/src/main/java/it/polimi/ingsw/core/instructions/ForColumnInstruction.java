package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.utils.IterableRange;

import java.util.Hashtable;
import java.util.Map;

public class ForColumnInstruction extends Instruction {
    /**
     * The name for the exposed variable mapping.
     */
    protected static final String COLUMN_VARIABLE_NAME = "column";

    /**
     * Maps the user-defined variable to the variables this class exposes.
     */
    protected Map<String, String> exposedVariableMapping;

    /**
     * @param columnVariableName The user-defined name for the row.
     */
    public ForColumnInstruction(String columnVariableName) {
        this.exposedVariableMapping = new Hashtable<>();
        this.exposedVariableMapping.put(COLUMN_VARIABLE_NAME, columnVariableName);
    }

    @Override
    public Integer run(Context context) {
        // Gets the user-defined name for the exposed variable 'column'
        String exposedName = exposedVariableMapping.get(COLUMN_VARIABLE_NAME);

        // Creates a snapshot for the sub-instructions
        Context.Snapshot snapshot = context.snapshot("for-column(" + exposedName + ")");

        // Retrieves the window from the context
        Window window = (Window) context.get(Context.WINDOW);

        // The results is the sum of each result of the iteration
        Integer result = new IterableRange<>(
                0,
                window.getCells()[0].length - 1,
                IterableRange.INTEGER_INCREMENT_FUNCTION
        ).stream().mapToInt(index -> {
                    // The exposed variable gets put inside the snapshot
                    snapshot.put(exposedName, getDiceInColumnFromWindow(window, index));

                    // Returns the result of the sub-instructions
                    return super.run(snapshot);
                }).sum();

        // Reverts the context to its original state
        snapshot.revert();

        return result;
    }

    /**
     * Returns the dice in the column at {@code index}.
     * @param window The window holding the dice.
     * @param index The index of the column containing the wanted dice.
     * @return The dice in the column at {@code index}.
     */
    private static Die[] getDiceInColumnFromWindow(Window window, int index) {
        Die[] columnDice = new Die[window.getCells()[index].length];

        for (int i = 0; i < columnDice.length; i++) {
            columnDice[i] = window.getCells()[i][index].getDie();
        }

        return columnDice;
    }
}
