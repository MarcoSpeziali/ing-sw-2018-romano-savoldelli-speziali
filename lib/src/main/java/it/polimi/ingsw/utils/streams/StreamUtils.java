package it.polimi.ingsw.utils.streams;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public final class StreamUtils {
    private StreamUtils() {
    }

    /**
     * @param predicate the predicate to negate
     * @param <T>       the parameter of the predicate
     * @return the negated version of {@code predicate}
     */
    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }
    
    /**
     * Inverts the parameters of the provided {@link BiConsumer}.
     *
     * @param biConsumer the {@link BiConsumer} to invert
     * @param <T> the first parameter class
     * @param <K> the second parameter class
     * @return a {@link BiConsumer} with the inverted parameters
     *         (i.e. the first parameters becomes {@link K} and the second parameter becomes {@link T})
     */
    public static <T, K> BiConsumer<K, T> invertBiConsumer(BiConsumer<T, K> biConsumer) {
        return (k, t) -> biConsumer.accept(t, k);
    }
    
    /**
     * Iterates over an array and provides the index and the element in the provided {@link BiConsumer}.
     *
     * @param array the array to iterate over
     * @param idxTBiConsumer a {@link BiConsumer} which accepts an index and an element
     * @param <T> the class of the element in the array
     */
    public static <T> void indexedForEach(final T[] array, BiConsumer<Integer, T> idxTBiConsumer) {
        for (int i = 0; i < array.length; i++) {
            idxTBiConsumer.accept(i, array[i]);
        }
    }
    
    /**
     * Iterates over a {@link Collection} and provides the index and the element in the provided {@link BiConsumer}.
     *
     * @param collection the {@link Collection} to iterate over
     * @param idxTBiConsumer a {@link BiConsumer} which accepts an index and an element
     * @param <T> the class of the element in the {@link Collection}
     */
    public static <T> void indexedForEach(final Collection<T> collection, BiConsumer<Integer, T> idxTBiConsumer) {
        final int collectionSize = collection.size();
    
        Iterator<T> collectionIterator = collection.iterator();
        
        for (int i = 0; i < collectionSize; i++) {
            idxTBiConsumer.accept(i, collectionIterator.next());
        }
    }
}
