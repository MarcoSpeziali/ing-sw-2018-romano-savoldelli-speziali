package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.utils.IterableRange;

import java.util.Hashtable;
import java.util.Map;
import java.util.Spliterator;
import java.util.stream.StreamSupport;

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
        String exposedName = exposedVariableMapping.get(COLUMN_VARIABLE_NAME);

        Context.Snapshot snapshot = context.snapshot("for-column(" + exposedName + ")");

        Window window = (Window) context.get(Context.WINDOW);

        Spliterator<Integer> indexSpliterator = new IterableRange<>(
                0,
                window.getCells()[0].length - 1,
                IterableRange.INTEGER_INCREMENT_FUNCTION
        ).spliterator();

        Integer result = StreamSupport.stream(indexSpliterator,false)
                .mapToInt(index -> {
                    snapshot.put(exposedName, getDiceInColumnFromWindow(window, index));

                    return super.run(snapshot);
                }).sum();

        snapshot.revert();

        return result;
    }

    private static Die[] getDiceInColumnFromWindow(Window window, int index) {
        Die[] columnDice = new Die[window.getCells()[index].length];

        for (int i = 0; i < columnDice.length; i++) {
            columnDice[i] = window.getCells()[i][index].getDie();
        }

        return columnDice;
    }
}
