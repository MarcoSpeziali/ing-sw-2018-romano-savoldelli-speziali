package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.server.concurrency.LockTarget;

import java.util.EnumMap;
import java.util.Map;

public class LockManager {

    private LockManager() {}

    private static Map<LockTarget, Object> lockObjects = new EnumMap<>(LockTarget.class);

    public static Object getLockObject(LockTarget target) {
        if (lockObjects.containsKey(target)) {
            Object o = new Object();
            lockObjects.put(target, o);
        }

        return lockObjects.get(target);
    }

    // TODO: add lock and release methods
}
