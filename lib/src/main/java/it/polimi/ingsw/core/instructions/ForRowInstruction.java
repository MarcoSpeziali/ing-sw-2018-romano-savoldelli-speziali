package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.utils.IterableRange;

import java.util.Hashtable;
import java.util.Map;
import java.util.Spliterator;
import java.util.stream.StreamSupport;

public class ForRowInstruction extends Instruction {
    /**
     * The name for the exposed variable mapping.
     */
    protected static final String ROW_VARIABLE_NAME = "die";

    /**
     * Maps the user-defined variable to the variables this class exposes.
     */
    protected Map<String, String> exposedVariableMapping;

    /**
     * @param rowVariableName The user-defined name for the row.
     */
    public ForRowInstruction(String rowVariableName) {
        this.exposedVariableMapping = new Hashtable<>();
        this.exposedVariableMapping.put(ROW_VARIABLE_NAME, rowVariableName);
    }

    @Override
    public Integer run(Context context) {
        String exposedName = exposedVariableMapping.get(ROW_VARIABLE_NAME);

        Context.Snapshot snapshot = context.snapshot("for-row(" + exposedName + ")");

        Window window = (Window) context.get(Context.WINDOW);

        Spliterator<Integer> indexSpliterator = new IterableRange<>(
                0,
                window.getCells().length - 1,
                IterableRange.INTEGER_INCREMENT_FUNCTION
        ).spliterator();

        Integer result = StreamSupport.stream(indexSpliterator,false)
                .mapToInt(index -> {
                    snapshot.put(exposedName, getDiceInRowFromWindow(window, index));

                    return super.run(snapshot);
                }).sum();

        snapshot.revert();

        return result;
    }

    private static Die[] getDiceInRowFromWindow(Window window, int index) {
        Die[] rowDice = new Die[window.getCells().length];

        for (int i = 0; i < rowDice.length; i++) {
            rowDice[i] = window.getCells()[index][i].getDie();
        }

        return rowDice;
    }
}
