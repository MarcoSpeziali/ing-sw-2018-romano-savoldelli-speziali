package it.polimi.ingsw.utils;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Represents a range of iterable values.
 *
 * @param <T>
 */
@SuppressWarnings("squid:S2055") // Range has a no-arg constructor
public class IterableRange<T extends Comparable<? super T> & Serializable> extends Range<T> implements Iterable<T>, Serializable {

    private static final long serialVersionUID = -4891369410626728370L;

    @FunctionalInterface
    public interface IncrementsProvider<R extends Comparable<? super R> & Serializable> extends Serializable {
        /**
         * Increments the parameter {@code previous}.
         * @return the incrementation of {@code previous}
         */
        R increment(R previous);
    }

    public static final IncrementsProvider<Integer>  INTEGER_INCREMENT_FUNCTION  = val -> ++val;
    public static final IncrementsProvider<Float>    FLOAT_INCREMENT_FUNCTION    = val -> ++val;
    public static final IncrementsProvider<Double>   DOUBLE_INCREMENT_FUNCTION   = val -> ++val;
    public static final IncrementsProvider<Long>     LONG_INCREMENT_FUNCTION     = val -> ++val;
    public static final IncrementsProvider<Byte>     BYTE_INCREMENT_FUNCTION     = val -> ++val;

    public static final IncrementsProvider<Integer>  INTEGER_DECREMENT_FUNCTION  = val -> --val;
    public static final IncrementsProvider<Float>    FLOAT_DECREMENT_FUNCTION    = val -> --val;
    public static final IncrementsProvider<Double>   DOUBLE_DECREMENT_FUNCTION   = val -> --val;
    public static final IncrementsProvider<Long>     LONG_DECREMENT_FUNCTION     = val -> --val;
    public static final IncrementsProvider<Byte>     BYTE_DECREMENT_FUNCTION     = val -> --val;

    /**
     * The functional function used to increment the value of type {@code T}.
     */
    protected final IncrementsProvider<T> incrementFunction;

    /**
     * Initializes {@code IterableRange<T>} with the starting and the ending value.
     *
     * @param start The stating value of the range.
     * @param end The ending value of the range.
     * @param incrementFunction The function used to increment the values.
     */
    public IterableRange(T start, T end, IncrementsProvider<T> incrementFunction) {
        super(start, end);
        Objects.requireNonNull(incrementFunction);

        this.incrementFunction = incrementFunction;
    }

    /**
     * Initializes {@code IterableRange<T>} from an existing {@code Range<T>}.
     * @param range The existing {@code Range<T>}.
     * @param incrementFunction The function used to increment the values.
     */
    public IterableRange(Range<T> range, IncrementsProvider<T> incrementFunction) {
        this(range == null ? null : range.start, range == null ? null : range.end, incrementFunction);
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
    public static <K extends Comparable<? super K> & Serializable> IterableRange<K> fromString(String range, String separator, Function<String, K> conversionProvider, IncrementsProvider<K> incrementFunction) {
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
    public static <K extends Comparable<? super K> & Serializable> IterableRange<K> singleValued(K value, IncrementsProvider<K> incrementFunction) {
        return new IterableRange<>(Objects.requireNonNull(
                Range.singleValued(value)
        ), incrementFunction);
    }

    /**
     * Creates an {@link Integer} {@link Range} as an unitary range.
     * @return An instance of {@link Range}
     */
    public static IterableRange<Integer> unitaryInteger() {
        return IterableRange.singleValued(1, INTEGER_INCREMENT_FUNCTION);
    }

    /**
     * Creates an {@link Float} {@link Range} as an unitary range.
     * @return An instance of {@link Range}
     */
    public static IterableRange<Float> unitaryFloat() {
        return IterableRange.singleValued(1F, FLOAT_INCREMENT_FUNCTION);
    }

    /**
     * Creates an {@link Double} {@link Range} as an unitary range.
     * @return An instance of {@link Range}
     */
    public static IterableRange<Double> unitaryDouble() {
        return IterableRange.singleValued(1D, DOUBLE_INCREMENT_FUNCTION);
    }

    /**
     * Creates an {@link Byte} {@link Range} as an unitary range.
     * @return An instance of {@link Range}
     */
    public static IterableRange<Byte> unitaryByte() {
        return IterableRange.singleValued((byte) 1, BYTE_INCREMENT_FUNCTION);
    }

    /**
     * Creates an {@link Long} {@link Range} as an unitary range.
     * @return An instance of {@link Range}
     */
    public static IterableRange<Long> unitaryLong() {
        return IterableRange.singleValued(1L, LONG_INCREMENT_FUNCTION);
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
    public Spliterator<T> spliterator() {
        return new RangeSpliterator(this, this.incrementFunction);
    }

    public Stream<T> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    public Stream<T> parallelStream() {
        return StreamSupport.stream(this.spliterator(), true);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IterableRange<?>)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        IterableRange<T> other = (IterableRange<T>) obj;

        //noinspection ConstantConditions
        return super.equals(other) && this.incrementFunction.equals(other.incrementFunction);
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
        private final IncrementsProvider<T> incrementFunction;

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
        RangeIterator(Range<T> range, IncrementsProvider<T> incrementFunction) {
            this.range = range;
            this.incrementFunction = incrementFunction;

            this.current = range.start;
        }

        @Override
        public boolean hasNext() {
            return !ranOnce || this.current.compareTo(Objects.requireNonNull(range.end)) < 0;
        }

        @Override
        public T next() {
            if (this.hasNext()) {
                T toReturn = this.current;
                ranOnce = true;

                this.current = incrementFunction.increment(this.current);
                return toReturn;
            }

            throw new NoSuchElementException();
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    private class RangeSpliterator implements Spliterator<T> {

        /**
         * The range to iterate through.
         */
        private final Range<T> range;

        /**
         * An unary operator that, given a value of type {@code T} returns the value incremented.
         */
        private final IncrementsProvider<T> incrementFunction;

        /**
         * The current value of the iterator sequence.
         */
        private T current;

        /**
         * {@code True} if the iterator has ran once.
         */
        private boolean ranOnce = false;

        /**
         * Creates a RangeIterator.
         * @param range The range to iterate through.
         * @param incrementFunction An unary operator that, given a value of type {@code T} returns the value incremented.
         */
        RangeSpliterator(Range<T> range, IncrementsProvider<T> incrementFunction) {
            this.range = range;
            this.incrementFunction = incrementFunction;

            this.current = range.start;
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            boolean hasNext = !ranOnce || this.current.compareTo(Objects.requireNonNull(range.end)) < 0;

            if (!ranOnce) {
                ranOnce = true;

                action.accept(current);
                return hasNext;
            }

            if (hasNext) {
                current = incrementFunction.increment(current);
                action.accept(current);
            }

            return hasNext;
        }

        @Override
        public Spliterator<T> trySplit() {
            return null;
        }

        @Override
        public long estimateSize() {
            return Long.MAX_VALUE;
        }

        @Override
        public int characteristics() {
            return NONNULL | IMMUTABLE | DISTINCT;
        }

        @Override
        public Comparator<? super T> getComparator() {
            return null;
        }
    }
}
