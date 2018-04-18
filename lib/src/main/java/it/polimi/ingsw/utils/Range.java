package it.polimi.ingsw.utils;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
     * Initializes `Range<T>` with the starting and the ending value.
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
     * Creates a `Range<K>` from a string representation.
     * @param range The string representation.
     * @param separator The separator between the two values.
     * @param conversionProvider A function that takes the string value and converts it to the desired object.
     * @param <K> The desired object type.
     * @return An instance of `Range<K>`
     */
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
     * Determines if the range is valid.
     *
     * @return `True` if range is valid, `false` otherwise.
     */
    private boolean isValid() {
        return start.compareTo(end) <= 0;
    }

    /**
     * Determines if the provided value is inside the range.
     * @param value The value to test.
     * @return `True` if the value is inside Range, `false` otherwise.
     */
    public boolean containsValue(T value) {
        return this.start.compareTo(value) <= 0 && value.compareTo(this.end) <= 0;
    }

    /**
     * Determines if this Range is inside the bounds of another range.
     * @param range The parent range to test on.
     * @return `True` if the current range is inside the provided one, `false` otherwise.
     */
    public boolean isInsideRange(Range<T> range) {
        return range.containsValue(this.start) && range.containsValue(this.end);
    }

    /**
     * Determines if another range is inside the bounds of this range.
     * @param range The child range to test.
     * @return `True` if the current range is inside the provided one, `false` otherwise.
     */
    public boolean containsRange(Range<T> range) {
        return this.containsValue(range.start) && this.containsValue(range.end);
    }

    /**
     * Presents the Range in readable format.
     *
     * @return The string representation of the Range.
     */
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
