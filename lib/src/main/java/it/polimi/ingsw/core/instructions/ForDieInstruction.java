package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Window;

import java.util.Hashtable;
import java.util.Map;

// TODO: docs
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
     * @param dieVariableName The user-defined name for the die.
     * @param filterShade The shade to filter.
     * @param filterColor The color to filter.
     */
    public ForDieInstruction(String dieVariableName, Integer filterShade, GlassColor filterColor) {
        this.filterShade = filterShade;
        this.filterColor = filterColor;

        this.exposedVariableMapping = new Hashtable<>();
        this.exposedVariableMapping.put(DIE_VARIABLE_NAME, dieVariableName);
    }

    @Override
    public Integer run(Context context) {
        String exposedName = exposedVariableMapping.get(DIE_VARIABLE_NAME);

        Context.Snapshot snapshot = context.snapshot("for-die(" + exposedName + ")");

        Window window = (Window) context.get(Context.WINDOW);

        Integer result = window.getDice().stream()
                .filter(die -> (
                        this.filterShade == 0 || die.getShade().equals(this.filterShade)) &&
                        (this.filterColor == null || die.getColor().equals(this.filterColor))
                ).mapToInt(die -> {
                    snapshot.put(exposedName, die);

                    return super.run(snapshot);
                }).sum();

        snapshot.revert();

        return result;
    }
}
