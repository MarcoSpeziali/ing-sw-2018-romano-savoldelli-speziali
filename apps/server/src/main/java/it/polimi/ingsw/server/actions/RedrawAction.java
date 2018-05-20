package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.Die;

import java.util.Random;

// TODO: docs
public class RedrawAction extends Action {

    private static final long serialVersionUID = -5834416569411220558L;
    private final VariableSupplier<Die> die;

    public RedrawAction(ActionData data, VariableSupplier<Die> die) {
        super(data);

        this.die = die;
    }

    @Override
    public Object run(Context context) {
        super.run(context);

        Random random = new Random(System.currentTimeMillis());

        // random.nextInt(int bound) returns a number in the set [0, bound)
        this.die.get(context).setShade(1 + random.nextInt(6));
        return null;
    }
}
