package it.polimi.ingsw.utils;

public interface Cache<K, V> {
    /**
     * Checks whether the cache contains a particular key.
     * @param key The key to test.
     * @return `True` if the cache contains the key, `false` otherwise.
     */
    boolean contains(K key);

    /**
     * Removes the value associated with a particular key.
     * @param key The value's key to remove.
     */
    void remove(K key);

    /**
     * Adds a value with a particular key.
     * @param key The key which identifies the value.
     * @param value The value to store.
     */
    void add(K key, V value);

    /**
     * Gets the value associated with a particular key.
     * @param key The value's key.
     * @return The value if the key exists in the cache, `null` otherwise.
     */
    V get(K key);

    /**
     * Gets the value associated with a particular key.
     * @param key The value's key.
     * @param defaultValue The default value.
     * @return The value if the key exists in the cache, a default value otherwise..
     */
    V getOrDefault(K key, V defaultValue);

    /**
     * Invalidates the cached objects.
     */
    void invalidate();
}
