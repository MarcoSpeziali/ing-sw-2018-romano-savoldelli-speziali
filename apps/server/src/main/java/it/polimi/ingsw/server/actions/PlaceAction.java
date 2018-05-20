package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.core.locations.ChoosablePutLocation;
import it.polimi.ingsw.server.utils.VariableSupplier;

// TODO: docs
public class PlaceAction extends Action {

    private static final long serialVersionUID = 8233234081672927366L;
    private final VariableSupplier<Die> die;
    private final VariableSupplier<ChoosablePutLocation> location;
    private final VariableSupplier<Integer> position;

    public PlaceAction(ActionData data, VariableSupplier<Die> die, VariableSupplier<ChoosablePutLocation> location, VariableSupplier<Integer> position) {
        super(data);

        this.die = die;
        this.location = location;
        this.position = position;
    }

    @Override
    public Object run(Context context) {
        super.run(context);

        this.location.get(context).putDie(this.die.get(context), this.position.get(context));
        return null;
    }
}
