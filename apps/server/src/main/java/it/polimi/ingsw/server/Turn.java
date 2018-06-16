package it.polimi.ingsw.server;

import it.polimi.ingsw.core.Player;
import it.polimi.ingsw.models.ToolCard;
import it.polimi.ingsw.server.managers.WorkflowManager;

import java.util.List;

import static it.polimi.ingsw.server.Turn.PerformedAction.*;


public class Turn {

    private final List<Turn> turns;
    private Player player;
    private PerformedAction state;
    private ToolCard toolCard;


    public Turn(Player currentPlayer, List<Turn> turns) {
        state = START;
        this.player = currentPlayer;
        this.turns = turns;
    }

    public void run() {

        while (true) {
            this.setState();
            if (state == END) {
                break;
            }

            else {
                if (toolCard.getEffect().getDescriptionKey().equals("non ricordo quale sia")) {
                    state = START;
                    WorkflowManager.getInstance().setCurrentTurn(WorkflowManager.getInstance().getCurrentTurn() - 1);
                }
                else {
                    state = END;
                }
                toolCard.activate();
            }
        }
    }


    public void setState() {

        player.addListener(toolCard -> {
            this.toolCard = toolCard;
            state = TOOLCARD;
        });
        player.addListener(() -> state = END);
    }

    public enum PerformedAction {
        START, TOOLCARD, END
    }


}
