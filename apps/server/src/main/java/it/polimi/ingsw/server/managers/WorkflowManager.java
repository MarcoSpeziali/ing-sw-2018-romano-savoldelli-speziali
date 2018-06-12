package it.polimi.ingsw.server.managers;

public class WorkflowManager {
    private static WorkflowManager ourInstance = new WorkflowManager();

    public static WorkflowManager getInstance() {
        return ourInstance;
    }

    private WorkflowManager() {
    }
}
