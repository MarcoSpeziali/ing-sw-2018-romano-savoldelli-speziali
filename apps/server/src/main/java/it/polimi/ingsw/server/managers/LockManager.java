package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.server.Constants;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LockManager {

    private static Map<Constants.LockTargets, Object> lockObjects = new EnumMap<>(Constants.LockTargets.class);

    static {
        lockObjects.putAll(
                Arrays.stream(Constants.LockTargets.values())
                        .collect(Collectors.toMap(o -> o, o -> new Object()))
        );
    }

    private LockManager() {
    }

    public static Object getLockObject(Constants.LockTargets target) {
        return lockObjects.get(target);
    }

    // TODO: add lock and release methods
}
