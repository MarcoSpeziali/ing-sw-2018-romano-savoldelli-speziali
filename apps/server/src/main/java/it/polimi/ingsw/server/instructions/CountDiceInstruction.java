package it.polimi.ingsw.server.instructions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Window;

import java.util.Objects;

public class CountDiceInstruction extends Instruction {

    /**
     * The filter for the dice shade.
     */
    private final Integer filterShade;

    /**
     * The filter for the dice color.
     */
    private final GlassColor filterColor;

    public CountDiceInstruction(Integer filterShade, GlassColor filterColor) {
        this.filterShade = filterShade;
        this.filterColor = filterColor;
    }

    @Override
    public Integer run(Context context) {
        // Retrieves the window from the context
        Window window = (Window) context.get(Context.WINDOW);

        // returns the count of dice in the window that satisfies the filters
        // Math.toIntExact because count returns a long
        return Math.toIntExact(
                window.getDice().stream()
                        .filter(Objects::nonNull)
                        .filter(die ->
                                (this.filterShade == 0 || die.getShade().equals(this.filterShade)) &&
                                        (this.filterColor == null || die.getColor().equals(this.filterColor))
                        ).count()
        );
    }
}
