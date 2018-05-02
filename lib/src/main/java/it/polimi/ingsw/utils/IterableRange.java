package it.polimi.ingsw.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Represents a range of iterable values.
 *
 * @param <T>
 */
public class IterableRange<T extends Comparable<? super T>> extends Range<T> implements Iterable<T> {

    // TODO: docs
    private static final UnaryOperator<Integer> INTEGER_INCREMENT = val -> ++val;
    private static final UnaryOperator<Float> FLOAT_INCREMENT = val -> ++val;
    private static final UnaryOperator<Double> DOUBLE_INCREMENT = val -> ++val;
    private static final UnaryOperator<Long> LONG_INCREMENT = val -> ++val;
    private static final UnaryOperator<Byte> BYTE_INCREMENT = val -> ++val;

    /**
     * The functional function used to increment the value of type {@code T}.
     */
    private final UnaryOperator<T> incrementFunction;

    /**
     * Initializes {@code IterableRange<T>} with the starting and the ending value.
     *
     * @param start The stating value of the range.
     * @param end The ending value of the range.
     * @param incrementFunction The function used to increment the values.
     */
    public IterableRange(T start, T end, UnaryOperator<T> incrementFunction) {
        super(start, end);

        this.incrementFunction = incrementFunction;
    }

    /**
     * Initializes {@code IterableRange<T>} from an existing {@code Range<T>}.
     * @param range The existing {@code Range<T>}.
     * @param incrementFunction The function used to increment the values.
     */
    public IterableRange(Range<T> range, UnaryOperator<T> incrementFunction) {
        this(range.start, range.end, incrementFunction);
    }

    /**
     * Creates a {@link Range} from a string representation.
     * @param range The string representation.
     * @param separator The separator between the two values.
     * @param conversionProvider A function that takes the string value and converts it to the desired object.
     * @param incrementFunction The function used to increment the values.
     * @param <K> The desired object type.
     * @return An instance of {@link Range}
     */
    @SuppressWarnings("WeakerAccess")
    public static <K extends Comparable<? super K>> IterableRange<K> fromString(String range, String separator, Function<String, K> conversionProvider, UnaryOperator<K> incrementFunction) {
        return new IterableRange<>(Objects.requireNonNull(
                Range.fromString(range, separator, conversionProvider)),
                incrementFunction
        );
    }

    /**
     * Creates a {@link Range} from a single value (i.e. {@link #getStart()} points to the same value as {@link #getEnd()})
     * @param value The starting and ending value.
     * @param incrementFunction The function used to increment the values.
     * @param <K> The desired object type.
     * @return An instance of {@link Range}
     */
    public static <K extends Comparable<? super K>> IterableRange<K> singleValued(K value, UnaryOperator<K> incrementFunction) {
        return new IterableRange<>(Objects.requireNonNull(
                Range.singleValued(value)
        ), incrementFunction);
    }

    /**
     * Creates an {@link Integer} {@link Range} as an unitary range.
     * @return An instance of {@link Range}
     */
    public static IterableRange<Integer> unitaryInteger() {
        return IterableRange.singleValued(1, INTEGER_INCREMENT);
    }

    /**
     * Creates an {@link Float} {@link Range} as an unitary range.
     * @return An instance of {@link Range}
     */
    public static IterableRange<Float> unitaryFloat() {
        return IterableRange.singleValued(1F, FLOAT_INCREMENT);
    }

    /**
     * Creates an {@link Double} {@link Range} as an unitary range.
     * @return An instance of {@link Range}
     */
    public static IterableRange<Double> unitaryDouble() {
        return IterableRange.singleValued(1D, DOUBLE_INCREMENT);
    }

    /**
     * Creates an {@link Byte} {@link Range} as an unitary range.
     * @return An instance of {@link Range}
     */
    public static IterableRange<Byte> unitaryByte() {
        return IterableRange.singleValued((byte) 1, BYTE_INCREMENT);
    }

    /**
     * Creates an {@link Long} {@link Range} as an unitary range.
     * @return An instance of {@link Range}
     */
    public static IterableRange<Long> unitaryLong() {
        return IterableRange.singleValued(1L, LONG_INCREMENT);
    }

    @Override
    public Iterator<T> iterator() {
        return new RangeIterator(this, this.incrementFunction);
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        for (T elem : this) {
            action.accept(elem);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IterableRange<?>)) {
            return false;
        }

        try {
            @SuppressWarnings("unchecked")
            IterableRange<T> other = (IterableRange<T>) obj;

            return super.equals(other) && this.incrementFunction.equals(other.incrementFunction);
        }
        catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.start, this.end, this.incrementFunction);
    }

    /**
     * The iterator used to iterate through a {@code IterableRange<T>}.
     */
    private class RangeIterator implements Iterator<T> {

        /**
         * The range to iterate through.
         */
        private final Range<T> range;

        /**
         * An unary operator that, given a value of type {@code T} returns the value incremented.
         */
        private final UnaryOperator<T> incrementFunction;

        /**
         * The current value of the iterator sequence.
         */
        private T current;

        /**
         * `True` if the iterator has ran once.
         */
        private boolean ranOnce = false;

        /**
         * Creates a RangeIterator.
         * @param range The range to iterate through.
         * @param incrementFunction An unary operator that, given a value of type {@code T} returns the value incremented.
         */
        RangeIterator(Range<T> range, UnaryOperator<T> incrementFunction) {
            this.range = range;
            this.incrementFunction = incrementFunction;

            this.current = range.start;
        }

        @Override
        public boolean hasNext() {
            return !ranOnce || this.current.compareTo(range.end) < 0;
        }

        @Override
        public T next() {
            if (this.hasNext()) {
                T toReturn = this.current;
                ranOnce = true;

                this.current = incrementFunction.apply(this.current);
                return toReturn;
            }

            throw new NoSuchElementException();
        }
    }
}
