package it.polimi.ingsw.utils;

import java.util.HashMap;

/**
 * Represents a memory-stored cache.
 * @param <K> The key type.
 * @param <V> The value type.
 */
public class InMemoryCache<K, V> implements Cache<K, V> {

    /**
     * An `HashMap` is used to create the cache.
     */
    private HashMap<K, V> hashMap;

    /**
     * Creates a memory-stored cache.
     */
    public InMemoryCache() {
        this.hashMap = new HashMap<>();
    }

    public boolean contains(K key) {
        return this.hashMap.containsKey(key);
    }

    public void remove(K key) {
        this.hashMap.remove(key);
    }

    public void add(K key, V value) {
        this.hashMap.put(key, value);
    }

    public V get(K key) {
        return this.getOrDefault(key, null);
    }

    public V getOrDefault(K key, V defaultValue) {
        return this.hashMap.getOrDefault(key, defaultValue);
    }

    public void invalidate() {
        this.hashMap.clear();
    }
}
