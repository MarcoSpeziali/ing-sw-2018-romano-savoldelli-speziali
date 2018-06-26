package it.polimi.ingsw.utils;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;

public final class ArrayUtils {
    private ArrayUtils() {}

    /**
     * Shuffles the provided array.
     *
     * @param array the original array
     * @param <T> the type of elements of the array
     * @return the shuffled array
     */
    public static <T> T[] shuffleArray(final T[] array) {
        T[] arrayCopy = Arrays.copyOf(array, array.length);

        Random random = new Random(System.currentTimeMillis());

        for (int i = arrayCopy.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);

            T value = arrayCopy[index];
            arrayCopy[index] = arrayCopy[i];
            arrayCopy[i] = value;
        }

        return arrayCopy;
    }

    /**
     * Slices the provided array.
     *
     * @param array the original array
     * @param from the initial index of the range to be copied, inclusive
     * @param to the final index of the range to be copied, exclusive.
     *     (This index may lie outside the array.)
     * @param <T> the class of the objects in the array
     * @return a sliced copy of the original array
     */
    public static <T> T[] sliceArray(final T[] array, int from, int to) {
        return Arrays.copyOfRange(array, from, to);
    }

    /**
     * Slices the provided array from the beginning.
     *
     * @param array the original array
     * @param to the final index of the range to be copied, exclusive.
     *     (This index may lie outside the array.)
     * @param <T> the class of the objects in the array
     * @return a sliced copy of the original array
     */
    public static <T> T[] sliceArray(final T[] array, int to) {
        return Arrays.copyOfRange(array, 0, to);
    }

    public static <T> void forEach(final T[] array, Consumer<T> consumer) {
        Arrays.stream(array)
                .forEach(consumer);
    }

    public static <T> void forEachMatrix(final T[][] matrix, Consumer<T> consumer) {
        Arrays.stream(matrix)
                .flatMap(Arrays::stream)
                .forEach(consumer);
    }
}
