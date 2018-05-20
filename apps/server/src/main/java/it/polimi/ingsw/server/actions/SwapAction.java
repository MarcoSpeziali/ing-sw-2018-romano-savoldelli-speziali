package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.server.utils.VariableSupplier;

// TODO: docs
public class SwapAction extends Action {

    private static final long serialVersionUID = 6960916269848753511L;
    protected final VariableSupplier<Die> die1;
    protected final VariableSupplier<Die> die2;

    public SwapAction(ActionData data, VariableSupplier<Die> die1, VariableSupplier<Die> die2) {
        super(data);

        this.die1 = die1;
        this.die2 = die2;
    }

    @Override
    public Object run(Context context) {
        super.run(context);

        GlassColor c1 = this.die1.get(context).getColor();
        Integer s1 = this.die1.get(context).getShade();

        this.die1.get(context).setColor(this.die2.get(context).getColor());
        this.die1.get(context).setShade(this.die2.get(context).getShade());

        this.die2.get(context).setColor(c1);
        this.die2.get(context).setShade(s1);

        return null;
    }
}
