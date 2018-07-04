package it.polimi.ingsw.server.instructions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.server.utils.VariableSupplier;

public class TakeShadeInstruction extends Instruction {

    private final VariableSupplier<Die> die;

    public TakeShadeInstruction(VariableSupplier<Die> die) {
        this.die = die;
    }

    @Override
    public Integer run(Context context) {
        Die contextDie = this.die.get(context);

        if (contextDie == null) {
            return 0;
        }

        // Selects the shade of the provided die as result
        return contextDie.getShade();
    }
}
