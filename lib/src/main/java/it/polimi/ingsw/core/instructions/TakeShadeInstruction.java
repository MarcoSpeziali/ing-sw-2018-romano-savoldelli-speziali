package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.actions.VariableSupplier;
import it.polimi.ingsw.models.Die;

public class TakeShadeInstruction extends Instruction {

    private final VariableSupplier<Die> die;

    public TakeShadeInstruction(VariableSupplier<Die> die) {
        this.die = die;
    }

    @Override
    public Integer run(Context context) {
        return this.die.get(context).getShade();
    }
}
