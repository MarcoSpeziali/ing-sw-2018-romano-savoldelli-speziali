package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.utils.MathUtils;

public class DecrementAction extends Action {

    private static final long serialVersionUID = -1530680862668890L;
    protected final VariableSupplier<Die> die;
    protected final VariableSupplier<Integer> by;

    public DecrementAction(ActionData data, VariableSupplier<Die> die, VariableSupplier<Integer> by) {
        super(data);

        this.die = die;
        this.by = by;
    }

    @Override
    public Object run(Context context) {
        super.run(context);

        Integer result = this.die.get(context).getShade() - this.by.get(context);
        int modularResult = MathUtils.modular(result, 6);

        this.die.get(context).setShade(modularResult == 0 ? 6 : modularResult);
        return null;
    }
}
