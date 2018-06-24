package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.server.Constants;

import java.util.HashMap;
import java.util.Map;

public class LockManager {
    
    /**
     * A {@link Map} containing the {@link Object}s associated with a {@link String} key.
     */
    private static Map<String, Object> lockObjects = new HashMap<>();

    private LockManager() {
        // hides the default public constructor
    }
    
    /**
     * @param target the {@link Constants.LockTargets} identifying the {@link Object} to retrieve
     * @return a {@link Object} that can be used to lock a resource
     */
    public static Object getLockObject(Constants.LockTargets target) {
        return getLockObject(target.toString());
    }
    
    /**
     * @param target the {@link Constants.LockTargets} partially identifying the {@link Object} to retrieve
     * @param uuid an {@literal int} id that fully identifies the {@link Object} to retrieve
     * @return a {@link Object} that can be used to lock a resource
     */
    public static Object getLockObject(Constants.LockTargets target, int uuid) {
        return getLockObject(String.format("%s-%d", target.toString(), uuid));
    }
    
    /**
     * @param target the {@link Constants.LockTargets} identifying the {@link Object} to delete
     */
    public static void removeLockObject(Constants.LockTargets target) {
        removeLockObject(target.toString());
    }
    
    /**
     * @param target the {@link Constants.LockTargets} partially identifying the {@link Object} to delete
     * @param uuid an {@literal int} id that fully identifies the {@link Object} to retrieve
     */
    public static void removeLockObject(Constants.LockTargets target, int uuid) {
        removeLockObject(String.format("%s-%d", target.toString(), uuid));
    }
    
    /**
     * @param key the key identifying the {@link Object} to retrieve
     * @return a {@link Object} that can be used to lock a resource
     */
    private static Object getLockObject(String key) {
        if (!lockObjects.containsKey(key)) {
            lockObjects.put(key, new Object());
        }
    
        return lockObjects.get(key);
    }
    
    /**
     * @param key the key identifying the {@link Object} to delete
     */
    private static void removeLockObject(String key) {
        lockObjects.remove(key);
    }
}
