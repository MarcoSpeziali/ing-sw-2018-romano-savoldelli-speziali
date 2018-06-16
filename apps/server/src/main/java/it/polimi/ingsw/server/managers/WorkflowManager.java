package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.server.Turn;

import java.util.LinkedList;
import java.util.List;

public class WorkflowManager {
    private static WorkflowManager ourInstance = new WorkflowManager();
    private int currentTurn;
    private List<Turn> turns = new LinkedList<>();

    private WorkflowManager() {
    }

    public static WorkflowManager getInstance() {
        return ourInstance;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    private void run() {
        while (currentTurn != turns.size()) {
            turns.get(currentTurn).run();
            currentTurn++;
        }
    }
}
