package it.polimi.ingsw.utils;

import java.util.HashMap;

/**
 * Represents a memory-stored cache.
 * @param <K> The key type.
 * @param <V> The value type.
 */
public class InMemoryCache<K, V> implements Cache<K, V> {

    /**
     * An {@code HashMap} is used to create the cache.
     */
    private HashMap<K, V> hashMap;

    /**
     * Creates a memory-stored cache.
     */
    public InMemoryCache() {
        this.hashMap = new HashMap<>();
    }

    @Override
    public boolean contains(K key) {
        return this.hashMap.containsKey(key);
    }

    @Override
    public void remove(K key) {
        this.hashMap.remove(key);
    }

    @Override
    public void add(K key, V value) {
        this.hashMap.put(key, value);
    }

    @Override
    public V get(K key) {
        return this.getOrDefault(key, null);
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        return this.hashMap.getOrDefault(key, defaultValue);
    }

    @Override
    public void invalidate() {
        this.hashMap.clear();
    }
}
