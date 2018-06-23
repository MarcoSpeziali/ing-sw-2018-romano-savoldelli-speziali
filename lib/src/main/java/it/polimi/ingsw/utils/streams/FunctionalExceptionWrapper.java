package it.polimi.ingsw.utils.streams;

import java.util.function.*;

public class FunctionalExceptionWrapper extends RuntimeException {

    private static final long serialVersionUID = 4262663147040028724L;

    private FunctionalExceptionWrapper(Exception exceptionToWrap) {
        super(exceptionToWrap);
    }

    /**
     * Wraps the generated exception.
     *
     * @param exceptionToWrap the exception to wrap
     */
    public static <T> T wrap(Exception exceptionToWrap) {
        throw new FunctionalExceptionWrapper(exceptionToWrap);
    }

    /**
     * Tries to unwrap the provided exception.
     *
     * @param exceptionClass the exception to unwrap
     * @param <T>            the exception to unwrap
     * @throws T if the exception has been successfully unwrapped
     */
    public <T extends Exception> FunctionalExceptionWrapper tryUnwrap(Class<T> exceptionClass) throws T {
        if (this.getCause().getClass().equals(exceptionClass)) {
            //noinspection unchecked
            throw (T) this.getCause();
        }

        return this;
    }

    /**
     * Tries to unwrap the provided exception, if fails a {@link RuntimeException} is thrown.
     *
     * @param exceptionClass the exception to unwrap
     * @param <T>            the exception to unwrap
     * @param <K>            the fake return value
     * @throws T if the exception has been successfully unwrapped
     */
    @SuppressWarnings("squid:S00112")
    public <T extends Exception, K> K tryFinalUnwrap(Class<T> exceptionClass) throws T {
        if (this.getCause().getClass().equals(exceptionClass)) {
            //noinspection unchecked
            throw (T) this.getCause();
        }

        throw new RuntimeException(this.getCause());
    }
    
    /**
     * Unwraps the exception as a {@link RuntimeException}.
     *
     * @param <K> the fake return value
     */
    @SuppressWarnings("squid:S00112")
    public <K> K unwrapRuntime() {
        throw new RuntimeException(this.getCause());
    }

    /**
     * Wraps an {@link UnsafeRunnable} into a {@link Runnable}.
     *
     * @param runnable the source {@link UnsafeRunnable}
     * @return the destination {@link Runnable}
     */
    public static Runnable wrap(UnsafeRunnable runnable) {
        return () -> {
            try {
                runnable.run();
            }
            catch (Exception e) {
                FunctionalExceptionWrapper.wrap(e);
            }
        };
    }

    /**
     * Wraps an {@link UnsafeConsumer} into a {@link Consumer}.
     *
     * @param unsafeConsumer the source {@link UnsafeConsumer}
     * @return the destination {@link Consumer}
     */
    public static <T> Consumer<T> wrap(UnsafeConsumer<T> unsafeConsumer) {
        return value -> {
            try {
                unsafeConsumer.accept(value);
            }
            catch (Exception e) {
                FunctionalExceptionWrapper.wrap(e);
            }
        };
    }

    /**
     * Wraps an {@link UnsafeBiConsumer} into a {@link BiConsumer}.
     *
     * @param unsafeBiConsumer the source {@link UnsafeBiConsumer}
     * @return the destination {@link BiConsumer}
     */
    public static <T, U> BiConsumer<T, U> wrap(UnsafeBiConsumer<T, U> unsafeBiConsumer) {
        return (t, u) -> {
            try {
                unsafeBiConsumer.accept(t, u);
            }
            catch (Exception e) {
                FunctionalExceptionWrapper.wrap(e);
            }
        };
    }

    /**
     * Wraps an {@link UnsafeSupplier} into a {@link Supplier}.
     *
     * @param unsafeSupplier the source {@link UnsafeSupplier}
     * @return the destination {@link Supplier}
     */
    public static <T> Supplier<T> wrap(UnsafeSupplier<T> unsafeSupplier) {
        return () -> {
            try {
                return unsafeSupplier.get();
            }
            catch (Exception e) {
                return FunctionalExceptionWrapper.wrap(e);
            }
        };
    }

    /**
     * Wraps an {@link UnsafeFunction} into a {@link Function}.
     *
     * @param unsafeFunction the source {@link UnsafeFunction}
     * @return the destination {@link Function}
     */
    public static <T, R> Function<T, R> wrap(UnsafeFunction<T, R> unsafeFunction) {
        return t -> {
            try {
                return unsafeFunction.apply(t);
            }
            catch (Exception e) {
                return FunctionalExceptionWrapper.wrap(e);
            }
        };
    }

    /**
     * Wraps an {@link UnsafeBiFunction} into a {@link BiFunction}.
     *
     * @param unsafeBiFunction the source {@link UnsafeBiFunction}
     * @return the destination {@link BiFunction}
     */
    public static <T, U, R> BiFunction<T, U, R> wrap(UnsafeBiFunction<T, U, R> unsafeBiFunction) {
        return (t, u) -> {
            try {
                return unsafeBiFunction.apply(t, u);
            }
            catch (Exception e) {
                return FunctionalExceptionWrapper.wrap(e);
            }
        };
    }

    /**
     * Makes the {@link Runnable} throws a {@link RuntimeException} if any {@link Exception} occurs
     *
     * @param unsafeRunnable the source {@link UnsafeRunnable}
     * @return the destination {@link Runnable}
     */
    public static Runnable unsafe(UnsafeRunnable unsafeRunnable) {
        try {
            return wrap(unsafeRunnable);
        }
        catch (FunctionalExceptionWrapper e) {
            return e.unwrapRuntime();
        }
    }

    /**
     * Makes the {@link Consumer} throws a {@link RuntimeException} if any {@link Exception} occurs
     *
     * @param unsafeConsumer the source {@link UnsafeConsumer}
     * @return the destination {@link Consumer}
     */
    public static <T> Consumer<T> unsafe(UnsafeConsumer<T> unsafeConsumer) {
        try {
            return wrap(unsafeConsumer);
        }
        catch (FunctionalExceptionWrapper e) {
            return e.unwrapRuntime();
        }
    }

    /**
     * Makes the {@link BiConsumer} throws a {@link RuntimeException} if any {@link Exception} occurs
     *
     * @param unsafeBiConsumer the source {@link UnsafeBiConsumer}
     * @return the destination {@link BiConsumer}
     */
    public static <T, U> BiConsumer<T, U> unsafe(UnsafeBiConsumer<T, U> unsafeBiConsumer) {
        try {
            return wrap(unsafeBiConsumer);
        }
        catch (FunctionalExceptionWrapper e) {
            return e.unwrapRuntime();
        }
    }

    /**
     * Makes the {@link Supplier} throws a {@link RuntimeException} if any {@link Exception} occurs
     *
     * @param unsafeSupplier the source {@link UnsafeSupplier}
     * @return the destination {@link Supplier}
     */
    public static <T> Supplier<T> unsafe(UnsafeSupplier<T> unsafeSupplier) {
        try {
            return wrap(unsafeSupplier);
        }
        catch (FunctionalExceptionWrapper e) {
            return e.unwrapRuntime();
        }
    }

    /**
     * Makes the {@link Function} throws a {@link RuntimeException} if any {@link Exception} occurs
     *
     * @param unsafeFunction the source {@link UnsafeFunction}
     * @return the destination {@link Function}
     */
    public static <T, R> Function<T, R> unsafe(UnsafeFunction<T, R> unsafeFunction) {
        try {
            return wrap(unsafeFunction);
        }
        catch (FunctionalExceptionWrapper e) {
            return e.unwrapRuntime();
        }
    }

    /**
     * Makes the {@link BiFunction} throws a {@link RuntimeException} if any {@link Exception} occurs
     *
     * @param unsafeBiFunction the source {@link UnsafeBiFunction}
     * @return the destination {@link BiFunction}
     */
    public static <T, U, R> BiFunction<T, U, R> unsafe(UnsafeBiFunction<T, U, R> unsafeBiFunction) {
        try {
            return wrap(unsafeBiFunction);
        }
        catch (FunctionalExceptionWrapper e) {
            return e.unwrapRuntime();
        }
    }

    @FunctionalInterface
    public interface UnsafeRunnable {
        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see     java.lang.Thread#run()
         */
        @SuppressWarnings("squid:S00112")
        void run() throws Exception;
    }

    @FunctionalInterface
    public interface UnsafeConsumer<T> {
        /**
         * Performs this operation on the given argument.
         *
         * @param t the input argument
         */
        @SuppressWarnings("squid:S00112")
        void accept(T t) throws Exception;
    }

    @FunctionalInterface
    public interface UnsafeBiConsumer<T, U> {
        /**
         * Performs this operation on the given arguments.
         *
         * @param t the first input argument
         * @param u the second input argument
         */
        @SuppressWarnings("squid:S00112")
        void accept(T t, U u) throws Exception;
    }

    @FunctionalInterface
    public interface UnsafeSupplier<T> {
        /**
         * Gets a result.
         *
         * @return a result
         */
        @SuppressWarnings("squid:S00112")
        T get() throws Exception;
    }

    @FunctionalInterface
    public interface UnsafeFunction<T, R> {
        /**
         * Applies this function to the given argument.
         *
         * @param t the function argument
         * @return the function result
         */
        @SuppressWarnings("squid:S00112")
        R apply(T t) throws Exception;
    }

    @FunctionalInterface
    public interface UnsafeBiFunction<T, U, R> {
        /**
         * Applies this function to the given arguments.
         *
         * @param t the first function argument
         * @param u the second function argument
         * @return the function result
         */
        @SuppressWarnings("squid:S00112")
        R apply(T t, U u) throws Exception;
    }
}
