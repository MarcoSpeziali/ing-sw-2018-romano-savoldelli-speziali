package it.polimi.ingsw.utils;

import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Represents a range of values.
 *
 * @param <T> The values type that must implement Comparable.
 */
public class Range<T extends Comparable<? super T> & Serializable> implements JSONSerializable {
    
    private static final long serialVersionUID = 3306661517507071620L;
    
    /**
     * The stating value of the range.
     */
    @SuppressWarnings("squid:S1948")
    protected final T start;
    
    /**
     * The ending value of the range.
     */
    @SuppressWarnings("squid:S1948")
    protected final T end;

    /**
     * Needed by {@link Serializable}.
     */
    protected Range() {
        this.start = null;
        this.end = null;
    }

    /**
     * Initializes {@link Range} with the starting and the ending value.
     *
     * @param start The stating value of the range.
     * @param end   The ending value of the range.
     */
    @JSONDesignatedConstructor
    public Range(
            @JSONElement("start") T start,
            @JSONElement("end") T end
    ) {
        this.start = Objects.requireNonNull(start);
        this.end = Objects.requireNonNull(end);

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

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("range-str", this.start + "::" + this.end);

        return jsonObject;
    }

    /**
     * Creates a {@link Range} from a string representation.
     *
     * @param range              The string representation.
     * @param separator          The separator between the two values.
     * @param conversionProvider A function that takes the string value and converts it to the desired object.
     * @param <K>                The desired object type.
     * @return An instance of {@link Range}
     */
    public static <K extends Comparable<? super K> & Serializable> Range<K> fromString(String range, String separator, Function<String, K> conversionProvider) {
        String[] tokens = range.split(Pattern.quote(separator));

        if (tokens.length == 1 && tokens[0].equals("") || tokens.length > 2 || tokens.length < 1) {
            return null;
        }

        if (tokens.length == 1) {
            K singleValue = conversionProvider.apply(tokens[0]);

            return new Range<>(singleValue, singleValue);
        }

        K start = conversionProvider.apply(tokens[0]);

        K end = conversionProvider.apply(tokens[1]);

        return new Range<>(start, end);
    }

    /**
     * Creates a {@link Range} from a single value (i.e. {@link #getStart()} points to the same value as {@link #getEnd()})
     *
     * @param value The starting and ending value.
     * @param <K>   The desired object type.
     * @return An instance of {@link Range}
     */
    public static <K extends Comparable<? super K> & Serializable> Range<K> singleValued(K value) {
        return new Range<>(value, value);
    }

    /**
     * Creates an {@link Integer} {@link Range} as an unitary range.
     *
     * @return An instance of {@link Range}
     */
    public static Range<Integer> unitaryInteger() {
        return Range.singleValued(1);
    }

    /**
     * Creates an {@link Float} {@link Range} as an unitary range.
     *
     * @return An instance of {@link Range}
     */
    public static Range<Float> unitaryFloat() {
        return Range.singleValued(1F);
    }

    /**
     * Creates an {@link Double} {@link Range} as an unitary range.
     *
     * @return An instance of {@link Range}
     */
    public static Range<Double> unitaryDouble() {
        return Range.singleValued(1D);
    }

    /**
     * Creates an {@link Byte} {@link Range} as an unitary range.
     *
     * @return An instance of {@link Range}
     */
    public static Range<Byte> unitaryByte() {
        return Range.singleValued((byte) 1);
    }

    /**
     * Creates an {@link Long} {@link Range} as an unitary range.
     *
     * @return An instance of {@link Range}
     */
    public static Range<Long> unitaryLong() {
        return Range.singleValued(1L);
    }

    /**
     * @return The stating value of the range.
     */
    @JSONElement("start")
    public T getStart() {
        return start;
    }

    /**
     * @return The ending value of the range.
     */
    @JSONElement("end")
    public T getEnd() {
        return end;
    }

    /**
     * Determines if the range is valid.
     *
     * @return {@code True} if range is valid, {@code false} otherwise.
     */
    private boolean isValid() {
        return start.compareTo(end) <= 0;
    }


    public boolean isSingleValued() {
        return this.start.equals(this.end);
    }

    public boolean isSingleValued(T wantedValue) {
        return isSingleValued() && this.start.equals(wantedValue);
    }

    /**
     * Determines if the provided value is inside the range.
     *
     * @param value The value to test.
     * @return {@code True} if the value is inside Range, {@code false} otherwise.
     */
    public boolean containsValue(T value) {
        return this.start.compareTo(value) <= 0 && value.compareTo(this.end) <= 0;
    }

    /**
     * Determines if this range is inside the bounds of another range.
     *
     * @param range The parent range to test on.
     * @return {@code True} if the current range is inside the provided one, {@code false} otherwise.
     */
    public boolean isInsideRange(Range<T> range) {
        return range.containsValue(this.start) && range.containsValue(this.end);
    }

    /**
     * Determines if another range is inside the bounds of this range.
     *
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Range<?> range = (Range<?>) o;

        return Objects.equals(start, range.start) &&
                Objects.equals(end, range.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.start, this.end);
    }
}
