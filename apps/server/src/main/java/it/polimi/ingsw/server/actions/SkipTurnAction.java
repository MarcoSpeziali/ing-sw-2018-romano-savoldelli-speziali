package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Player;
import it.polimi.ingsw.server.managers.turns.PlayerTurnList;
import it.polimi.ingsw.server.utils.VariableSupplier;

public class SkipTurnAction extends Action {
    
    private static final long serialVersionUID = -7395997444394564125L;
    
    private final VariableSupplier<Byte> round;
    private final VariableSupplier<Byte> turn;
    private final VariableSupplier<Player> playerVariableSupplier;
    
    public SkipTurnAction(ActionData data, VariableSupplier<Byte> round, VariableSupplier<Byte> turn, VariableSupplier<Player> playerVariableSupplier) {
        super(data);
    
        this.round = round;
        this.turn = turn;
        this.playerVariableSupplier = playerVariableSupplier;
    }
    
    @Override
    public Object run(Context context) {
        PlayerTurnList playerTurnList = (PlayerTurnList) context.get(Context.TURN_LIST);
        playerTurnList.skipPlayerTurn(
                this.playerVariableSupplier.get(context).getPlayer(),
                round.get(context),
                turn.get(context)
        );
        
        return null;
    }
}
