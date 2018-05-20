package it.polimi.ingsw.utils.streams;

import java.util.function.Predicate;

public final class StreamUtils {
    private StreamUtils() {}

    /**
     * @param predicate the predicate to negate
     * @param <T> the parameter of the predicate
     * @return the negated version of {@code predicate}
     */
    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }
}
