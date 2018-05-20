package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.locations.ChooseLocation;

import java.util.Random;

// TODO: docs
public class RedrawAllAction extends Action {

    private static final long serialVersionUID = 7305416979511806520L;
    private final VariableSupplier<ChooseLocation> location;

    public RedrawAllAction(ActionData data, VariableSupplier<ChooseLocation> location) {
        super(data);

        this.location = location;
    }

    @Override
    public Object run(Context context) {
        super.run(context);

        Random random = new Random(System.currentTimeMillis());

        this.location.get(context).getDice().forEach(die -> die.setShade(1 + random.nextInt(6)));
        return null;
    }
}
