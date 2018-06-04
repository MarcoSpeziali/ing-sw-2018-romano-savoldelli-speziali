package it.polimi.ingsw.core;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Context implements Serializable {

    private static final long serialVersionUID = -3619852381252122217L;

    public static final String WINDOW = "window";
    public static final String DRAFT_POOL = "draft_pool";
    public static final String BAG = "bag";
    public static final String ROUND_TRACK = "round_track";
    public static final String CURRENT_PLAYER = "current_player";
    public static final String MATCH = "match";

    /**
     * The context's variables are stored into an {@link HashMap}.
     */
    @SuppressWarnings("WeakerAccess")
    protected HashMap<String, Object> hashMap;

    /**
     * The singleton of the {@link Context}.
     */
    private static Context singleton;

    /**
     * @return The shared instance of the {@link Context} class.
     */
    public static Context getSharedInstance() {
        if (singleton == null) {
            singleton = new Context();
        }

        return singleton;
    }

    protected Context() {
        super();

        this.hashMap = new HashMap<>();
    }

    /**
     * Returns {@code true} if this map contains a mapping for the specified
     * key.  More formally, returns {@code true} if and only if
     * this map contains a mapping for a key {@code k} such that
     * {@code Objects.equals(key, k)}.  (There can be
     * at most one such mapping.)
     *
     * @param key key whose presence in this map is to be tested
     * @return {@code true} if this map contains a mapping for the specified
     * key
     * @throws ClassCastException   if the key is of an inappropriate type for
     *                              this map
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    public boolean containsKey(String key) {
        return this.hashMap.containsKey(key.toLowerCase());
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that
     * {@code Objects.equals(key, k)},
     * then this method returns {@code v}; otherwise
     * it returns {@code null}.  (There can be at most one such mapping.)
     *
     * <p>If this map permits null values, then a return value of
     * {@code null} does not <i>necessarily</i> indicate that the map
     * contains no mapping for the key; it's also possible that the map
     * explicitly maps the key to {@code null}.  The {@link #containsKey
     * containsKey} operation may be used to distinguish these two cases.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     * {@code null} if this map contains no mapping for the key
     * @throws ClassCastException   if the key is of an inappropriate type for
     *                              this map
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    public Object get(String key) {
        return this.hashMap.get(key.toLowerCase());
    }

    /**
     * Associates the specified value with the specified key in this map
     * (optional operation).  If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value.  (A map
     * {@code m} is said to contain a mapping for a key {@code k} if and only
     * if {@link #containsKey(String) m.containsKey(k)} would return
     * {@code true}.)
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with {@code key}, or
     * {@code null} if there was no mapping for {@code key}.
     * (A {@code null} return can also indicate that the map
     * previously associated {@code null} with {@code key},
     * if the implementation supports {@code null} values.)
     * @throws UnsupportedOperationException if the {@code put} operation
     *                                       is not supported by this map
     * @throws ClassCastException            if the class of the specified key or value
     *                                       prevents it from being stored in this map
     * @throws NullPointerException          if the specified key or value is null
     *                                       and this map does not permit null keys or values
     * @throws IllegalArgumentException      if some property of the specified key
     *                                       or value prevents it from being stored in this map
     */
    public Object put(String key, Object value) {
        return this.hashMap.put(key.toLowerCase(), value);
    }

    /**
     * Returns a {@link Set} view of the keys contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own {@code remove} operation), the results of
     * the iteration are undefined.  The set supports element removal,
     * which removes the corresponding mapping from the map, via the
     * {@code Iterator.remove}, {@code Set.remove},
     * {@code removeAll}, {@code retainAll}, and {@code clear}
     * operations.  It does not support the {@code add} or {@code addAll}
     * operations.
     *
     * @return a set view of the keys contained in this map
     */
    public Set<String> keySet() {
        return this.hashMap.keySet();
    }

    /**
     * Returns a {@link Collection} view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are
     * reflected in the collection, and vice-versa.  If the map is
     * modified while an iteration over the collection is in progress
     * (except through the iterator's own {@code remove} operation),
     * the results of the iteration are undefined.  The collection
     * supports element removal, which removes the corresponding
     * mapping from the map, via the {@code Iterator.remove},
     * {@code Collection.remove}, {@code removeAll},
     * {@code retainAll} and {@code clear} operations.  It does not
     * support the {@code add} or {@code addAll} operations.
     *
     * @return a collection view of the values contained in this map
     */
    public Collection<Object> values() {
        return this.hashMap.values();
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own {@code remove} operation, or through the
     * {@code setValue} operation on a map entry returned by the
     * iterator) the results of the iteration are undefined.  The set
     * supports element removal, which removes the corresponding
     * mapping from the map, via the {@code Iterator.remove},
     * {@code Set.remove}, {@code removeAll}, {@code retainAll} and
     * {@code clear} operations.  It does not support the
     * {@code add} or {@code addAll} operations.
     *
     * @return a set view of the mappings contained in this map
     */
    public Set<Map.Entry<String, Object>> entrySet() {
        return this.hashMap.entrySet();
    }

    /**
     * Returns the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key
     * @throws ClassCastException   if the key is of an inappropriate type for
     *                              this map
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @implSpec The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     * @since 1.8
     */
    public Object getOrDefault(String key, Object defaultValue) {
        return this.hashMap.getOrDefault(key.toLowerCase(), defaultValue);
    }

    /**
     * Performs the given action for each entry in this map until all entries
     * have been processed or the action throws an exception.   Unless
     * otherwise specified by the implementing class, actions are performed in
     * the order of entry set iteration (if an iteration order is specified.)
     * Exceptions thrown by the action are relayed to the caller.
     *
     * @param action The action to be performed for each entry
     * @throws NullPointerException            if the specified action is null
     * @throws java.util.ConcurrentModificationException if an entry is found to be
     *                                         removed during iteration
     * @implSpec The default implementation is equivalent to, for this {@code map}:
     * <pre> {@code
     * for (Map.Entry<K, V> entry : map.entrySet())
     *     action.accept(entry.getKey(), entry.getValue());
     * }</pre>
     * <p>
     * The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     * @since 1.8
     */
    public void forEach(BiConsumer<? super String, ? super Object> action) {
        this.hashMap.forEach(action);
    }

    /**
     * Creates a snapshot of the current state of the context. So every key created by the snapshot can
     * be removed by calling {@code ContextSnapshot::revert}.
     * @param snapshotId The id of the snapshot.
     * @return A new context with the same variables of the original one.
     */
    public Snapshot snapshot(String snapshotId) {
        return new Snapshot(snapshotId, this);
    }

    /**
     * Creates a snapshot of the current state of the context, executed the provided action and reverts the snapshot.
     * So every key created by the snapshot gets immediately removed.
     * @param snapshotId The id of the snapshot.
     * @param action A {@link Consumer<Snapshot>} that consumes the snapshot.
     */
    @SuppressWarnings("UnusedReturnValue")
    public Context snapshot(String snapshotId, Consumer<Snapshot> action) {
        Snapshot snapshot = new Snapshot(snapshotId, this);

        action.accept(snapshot);

        return snapshot.revert();
    }

    public class Snapshot extends Context {

        private static final long serialVersionUID = -24066494050937271L;

        /**
         * The id of the snapshot.
         */
        private final String snapshotId;

        /**
         * The parent context.
         */
        private final Context parentContext;

        /**
         * Creates a snapshot of the current state of the context. So every key created by the snapshot can
         * be removed by calling {@code ContextSnapshot::revert}.
         * @param snapshotId The id of the snapshot.
         */
        private Snapshot(String snapshotId, Context parentContext) {
            this.snapshotId = snapshotId;
            this.parentContext = parentContext;
            this.hashMap = parentContext.hashMap;
        }

        @Override
        public boolean containsKey(String key) {
            if (parentContext.containsKey(this.snapshotId + "::" + key)) {
                return true;
            }

            return parentContext.containsKey(key);
        }

        @Override
        public Object get(String key) {
            Object result = parentContext.get(this.snapshotId + "::" + key);

            return result == null ? parentContext.get(key) : result;
        }

        @Override
        public Object put(String key, Object value) {
            return parentContext.put(this.snapshotId + "::" + key, value);
        }

        @Override
        public Object getOrDefault(String key, Object defaultValue) {
            if (parentContext.containsKey(this.snapshotId + "::" + key)) {
                return parentContext.get(this.snapshotId + "::" + key);
            }

            return parentContext.getOrDefault(key, defaultValue);
        }

        @Override
        public Snapshot snapshot(String snapshotId) {
            return new Snapshot(snapshotId, this);
        }

        /**
         * Reverts the state of context to the latest snapshot.
         */
        public Context revert() {
            Set<String> keys = this.hashMap.keySet().stream()
                    .filter(key -> key.startsWith(this.snapshotId + "::"))
                    .collect(Collectors.toSet());

            for (String key : keys) {
                this.hashMap.remove(key);
            }

            return this.parentContext;
        }

        @Override
        public String toString() {
            return this.parentContext == Context.getSharedInstance() ?
                    this.snapshotId :
                    this.parentContext + "::" + this.snapshotId;
        }
    }
}
