package it.polimi.ingsw.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Represents a range of iterable values.
 *
 * @param <T>
 */
public class IterableRange<T extends Comparable<? super T>> extends Range<T> implements Iterable<T> {

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
