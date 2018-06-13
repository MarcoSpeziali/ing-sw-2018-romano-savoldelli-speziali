package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.core.Player;
import it.polimi.ingsw.server.Turn;

import java.util.LinkedList;
import java.util.List;

public class WorkflowManager {
    private static WorkflowManager ourInstance = new WorkflowManager();

    public static WorkflowManager getInstance() {
        return ourInstance;
    }

    private int currentTurn;

    public int getCurrentTurn() {
        return currentTurn;
    }

    private List<Turn> turns = new LinkedList<>();

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    private WorkflowManager() {
    }

    private void run() {
        while(currentTurn != turns.size()) {
            turns.get(currentTurn).run();
            currentTurn++;
        }
    }
}
