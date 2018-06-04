package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.server.Constants;

import java.util.HashMap;
import java.util.Map;

public final class ThreadManager {

    private ThreadManager() {}

    private static Map<Constants.Threads, Thread> threadMap = new HashMap<>();

    public static Thread addThread(Constants.Threads threadType, Thread thread) {
        threadMap.put(threadType, thread);

        return thread;
    }

    public static Thread addThread(Constants.Threads threadType, Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName(threadType.getName());

        return addThread(threadType, thread);
    }

    public static Thread getThread(Constants.Threads threadType) {
        return threadMap.get(threadType);
    }
}
