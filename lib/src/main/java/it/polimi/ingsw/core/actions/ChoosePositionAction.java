package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.core.locations.ChooseLocation;

public class ChoosePositionAction extends Action {

    private final UserInteractionProvider userInteractionProvider;
    private final ChooseLocation from;

    public ChoosePositionAction(ActionData data, UserInteractionProvider userInteractionProvider, ChooseLocation from) {
        super(data);

        this.userInteractionProvider = userInteractionProvider;
        this.from = from;
    }

    @Override
    public Object run(Context context) {
        super.run(context);

        return this.userInteractionProvider.choosePosition(this.from);
    }
}
