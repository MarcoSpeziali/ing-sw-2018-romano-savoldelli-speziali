package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.server.Constants;

import java.util.*;

// TODO: docs
public final class ThreadManager {

    // TODO: use a Tree
    private static TreeMap<TreeKey, Thread> threadTree = new TreeMap<>();
    private static Map<Constants.Threads, Thread> threadMap = new EnumMap<>(Constants.Threads.class);

    private ThreadManager() {
    }

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

    private static class TreeKey {
        private Thread thread;
        private Constants.Threads threadType;

        public TreeKey(Thread thread, Constants.Threads threadType) {
            this.thread = thread;
            this.threadType = threadType;
        }

        public Thread getThread() {
            return thread;
        }

        public Constants.Threads getThreadType() {
            return threadType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            TreeKey treeKey = (TreeKey) o;
            return Objects.equals(thread, treeKey.thread) &&
                    threadType == treeKey.threadType;
        }

        @Override
        public int hashCode() {
            return Objects.hash(thread, threadType);
        }
    }

    public static class ThreadNode {
        private Thread thread;
        private Constants.Threads threadType;
        private ThreadNode parent;
        private List<ThreadNode> children;

        public ThreadNode(Constants.Threads threadType, Thread thread, ThreadNode parent) {
            this.threadType = threadType;
            this.thread = thread;
            this.parent = parent;
            this.children = new LinkedList<>();
        }

        public ThreadNode(Constants.Threads threadType, Thread thread) {
            this(threadType, thread, null);
        }
    }
}
