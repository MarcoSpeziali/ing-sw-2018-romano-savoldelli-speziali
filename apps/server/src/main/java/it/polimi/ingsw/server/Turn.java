package it.polimi.ingsw.server;

import it.polimi.ingsw.core.Player;
import it.polimi.ingsw.listeners.OnDiePickedListener;
import it.polimi.ingsw.listeners.OnDiePutListener;
import it.polimi.ingsw.models.ToolCard;

public class Turn {

    private Player player;
    private static State state;


    public Turn(Player currentPlayer) {
        state = State.START;
        this.player = currentPlayer;
    }

    public void run() {
        player.addListener((OnDiePickedListener) (d, l) -> {
            state.end();
        });

        player.addListener((OnDiePutListener) (d, l) -> {
            state.end();
        });

        player.addListener((toolCard) -> {
            state.toolCard();
            state.execute(toolCard);
        });

    }

    private interface StateInterface {
        default StateInterface toolCard(){
            return this;
        }
        default StateInterface end() {
            return this;
        }
        default StateInterface execute(ToolCard toolCard) {
            return this;
        }
    }

    private enum State implements StateInterface{
        START("start") {
            public State toolCard() {
                return TOOLCARD;
            }
            public State end() {
                return END;
            }
        },

        TOOLCARD("toolcard") {
            public State execute(ToolCard toolCard) {
                toolCard.activate();
                // TODO handle different toolcard
                return END;
            }
        },

        END("end");

        State(String string) {
        }
    }


}
