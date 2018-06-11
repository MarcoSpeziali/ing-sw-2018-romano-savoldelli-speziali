package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.server.Constants;

import java.util.EnumMap;
import java.util.Map;

// TODO: docs
public final class ThreadManager {

    private ThreadManager() {}
    
    // TODO: use a Tree
    
    private static Map<Constants.Threads, Thread> threadMap = new EnumMap<>(Constants.Threads.class);

    public static Thread addThread(Constants.Threads threadType, Thread thread) {
        threadMap.put(threadType, thread);

        return thread;
    }

    public static Thread addThread(Constants.Threads threadType, Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName(threadType.toString());

        return addThread(threadType, thread);
    }

    public static Thread getThread(Constants.Threads threadType) {
        return threadMap.get(threadType);
    }
}
