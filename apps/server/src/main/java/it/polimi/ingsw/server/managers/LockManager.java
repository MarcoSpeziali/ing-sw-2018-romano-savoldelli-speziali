package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.server.Constants;

import java.util.EnumMap;
import java.util.Map;

public class LockManager {

    private static Map<Constants.LockTargets, Object> lockObjects = new EnumMap<>(Constants.LockTargets.class);

    private LockManager() {
    }

    public static Object getLockObject(Constants.LockTargets target) {
        if (lockObjects.containsKey(target)) {
            Object o = new Object();
            lockObjects.put(target, o);
        }

        return lockObjects.get(target);
    }

    // TODO: add lock and release methods
}
