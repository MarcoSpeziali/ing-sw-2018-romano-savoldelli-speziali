package it.polimi.ingsw.utils.streams;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

class FunctionalExceptionWrapperTest {

    @Test
    void testSingleWrap() {
        try {
            //noinspection ResultOfMethodCallIgnored
            IntStream.range(0, 10).map(operand -> {
                try {
                    return singleThrowableMethod();
                }
                catch (ClassNotFoundException e) {
                    return FunctionalExceptionWrapper.wrap(e);
                }
            }).toArray();
        }
        catch (FunctionalExceptionWrapper e) {
            Assertions.assertThrows(ClassNotFoundException.class, () -> e.tryUnwrap(ClassNotFoundException.class));
        }
    }

    @Test
    void testMultipleWrap() throws IllegalAccessException {
        try {
            //noinspection ResultOfMethodCallIgnored
            IntStream.range(0, 10).map(operand -> {
                try {
                    return multipleThrowableMethod();
                }
                catch (ClassNotFoundException | IllegalAccessException e) {
                    return FunctionalExceptionWrapper.wrap(e);
                }
            }).toArray();
        }
        catch (FunctionalExceptionWrapper e) {
            e.tryUnwrap(IllegalAccessException.class);

            Assertions.assertThrows(ClassNotFoundException.class, () -> e.tryUnwrap(ClassNotFoundException.class));
        }
    }

    @Test
    void testMultipleWrapChain() {
        try {
            //noinspection ResultOfMethodCallIgnored
            IntStream.range(0, 10).map(operand -> {
                try {
                    return multipleThrowableMethod();
                }
                catch (ClassNotFoundException | IllegalAccessException e) {
                    return FunctionalExceptionWrapper.wrap(e);
                }
            }).toArray();
        }
        catch (FunctionalExceptionWrapper e) {
            Assertions.assertThrows(ClassNotFoundException.class, () -> e.tryUnwrap(IllegalAccessException.class)
                    .tryUnwrap(ClassNotFoundException.class));
        }
    }

    @Test
    void testFinalChainWrap() {
        try {
            //noinspection ResultOfMethodCallIgnored
            IntStream.range(0, 10).map(operand -> {
                try {
                    return multipleThrowableMethod();
                }
                catch (Exception e) {
                    return FunctionalExceptionWrapper.wrap(e);
                }
            }).toArray();
        }
        catch (FunctionalExceptionWrapper e) {
            Assertions.assertThrows(RuntimeException.class, () -> e.tryUnwrap(IllegalAccessException.class)
                    .tryFinalUnwrap(IllegalArgumentException.class));
        }
    }

    private int singleThrowableMethod() throws ClassNotFoundException {
        throw new ClassNotFoundException();
    }

    private int multipleThrowableMethod() throws ClassNotFoundException, IllegalAccessException {
        throw new ClassNotFoundException();
    }
}