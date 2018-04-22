package it.polimi.ingsw.utils;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Represents a range of values.
 *
 * @param <T> The values type that must implement Comparable.
 */
public class Range<T extends Comparable<? super T>> {
    /**
     * The stating value of the range.
     */
    protected final T start;

    /**
     * @return The stating value of the range.
     */
    public T getStart() {
        return start;
    }

    /**
     * The ending value of the range.
     */
    protected final T end;

    /**
     * @return The ending value of the range.
     */
    public T getEnd() {
        return end;
    }

    /**
     * Initializes {@link Range} with the starting and the ending value.
     *
     * @param start The stating value of the range.
     * @param end The ending value of the range.
     */
    public Range(T start, T end) {
        this.start = start;
        this.end = end;

        if (!this.isValid()) {
            throw new IllegalArgumentException(
                    String.format(
                            "The specified range is invalid. %s is greater or equal than %s. " +
                            "The starting value must be less or equal than the ending one.",
                            this.start,
                            this.end
                    )
            );
        }
    }

    /**
     * Creates a {@link Range} from a string representation.
     * @param range The string representation.
     * @param separator The separator between the two values.
     * @param conversionProvider A function that takes the string value and converts it to the desired object.
     * @param <K> The desired object type.
     * @return An instance of {@link Range}
     */
    @SuppressWarnings("WeakerAccess")
    public static <K extends Comparable<? super K>> Range<K> fromString(String range, String separator, Function<String, K> conversionProvider) {
        String[] tokens = range.split(Pattern.quote(separator));

        if (tokens.length == 1 && tokens[0].equals("")) {
            return null;
        }

        if (tokens.length == 1) {
            K singleValue = conversionProvider.apply(tokens[0]);

            return new Range<>(singleValue, singleValue);
        }

        Object[] convertedTokens = Arrays.stream(tokens)
                .map(conversionProvider)
                .toArray();

        @SuppressWarnings("unchecked")
        K start = (K) convertedTokens[0];

        @SuppressWarnings("unchecked")
        K end = (K) convertedTokens[1];

        return new Range<>(start, end);
    }

    /**
     * Creates a {@link Range} from a single value (i.e. {@link #getStart()} points to the same value as {@link #getEnd()})
     * @param value The starting and ending value.
     * @param <K> The desired object type.
     * @return An instance of {@link Range}
     */
    public static <K extends Comparable<? super K>> Range<K> singleValued(K value) {
        return new Range<>(value, value);
    }

    /**
     * Determines if the range is valid.
     *
     * @return {@code True} if range is valid, {@code false} otherwise.
     */
    private boolean isValid() {
        return start.compareTo(end) <= 0;
    }

    /**
     * Determines if the provided value is inside the range.
     * @param value The value to test.
     * @return {@code True} if the value is inside Range, {@code false} otherwise.
     */
    public boolean containsValue(T value) {
        return this.start.compareTo(value) <= 0 && value.compareTo(this.end) <= 0;
    }

    /**
     * Determines if this range is inside the bounds of another range.
     * @param range The parent range to test on.
     * @return {@code True} if the current range is inside the provided one, {@code false} otherwise.
     */
    public boolean isInsideRange(Range<T> range) {
        return range.containsValue(this.start) && range.containsValue(this.end);
    }

    /**
     * Determines if another range is inside the bounds of this range.
     * @param range The child range to test.
     * @return {@code True} if the current range is inside the provided one, {@code false} otherwise.
     */
    public boolean containsRange(Range<T> range) {
        return this.containsValue(range.start) && this.containsValue(range.end);
    }

    @Override
    public String toString() {
        return String.format("[%s - %s]", this.start, this.end);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Range<?>)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        Range<T> o = (Range<T>) obj;

        return o.start == this.start && o.end == this.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.start, this.end);
    }
}
