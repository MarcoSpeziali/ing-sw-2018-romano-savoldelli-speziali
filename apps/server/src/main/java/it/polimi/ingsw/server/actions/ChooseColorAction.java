package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.locations.ChooseLocation;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.server.utils.VariableSupplier;

public class ChooseColorAction extends ChooseDieAction {

    private static final long serialVersionUID = -1654067241403388125L;

    public ChooseColorAction(ActionData data, VariableSupplier<ChooseLocation> from, VariableSupplier<Integer> shade) {
        super(data, from, context -> null, shade);
    }

    @Override
    public Object run(Context context) {
        return ((Die) super.run(context)).getColor();
    }
}
