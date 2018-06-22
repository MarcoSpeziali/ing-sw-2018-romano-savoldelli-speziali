package it.polimi.ingsw.utils.streams;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ExceptionWrapper extends RuntimeException {

    private static final long serialVersionUID = 4262663147040028724L;

    private ExceptionWrapper(Exception exceptionToWrap) {
        super(exceptionToWrap);
    }

    /**
     * Wraps the generated exception.
     *
     * @param exceptionToWrap the exception to wrap
     */
    public static <T> T wrap(Exception exceptionToWrap) {
        throw new ExceptionWrapper(exceptionToWrap);
    }

    /**
     * Tries to unwrap the provided exception.
     *
     * @param exceptionClass the exception to unwrap
     * @param <T>            the exception to unwrap
     * @throws T if the exception has been successfully unwrapped
     */
    public <T extends Exception> ExceptionWrapper tryUnwrap(Class<T> exceptionClass) throws T {
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
    
    // TODO: docs
    public static Runnable unchecked(UncheckedRunnable uncheckedRunnable) {
        try {
            return () -> {
                try {
                    uncheckedRunnable.run();
                }
                catch (Exception e) {
                    ExceptionWrapper.wrap(e);
                }
            };
        }
        catch (ExceptionWrapper e) {
            return e.unwrapRuntime();
        }
    }
    
    // TODO: docs
    public static <T> Consumer<T> unchecked(UncheckedConsumer<T> uncheckedConsumer) {
        try {
            return value -> {
                try {
                    uncheckedConsumer.accept(value);
                }
                catch (Exception e) {
                    ExceptionWrapper.wrap(e);
                }
            };
        }
        catch (ExceptionWrapper e) {
            return e.unwrapRuntime();
        }
    }
    
    // TODO: docs
    public static <T, U> BiConsumer<T, U> unchecked(UncheckedBiConsumer<T, U> uncheckedBiConsumer) {
        try {
            return (t, u) -> {
                try {
                    uncheckedBiConsumer.accept(t, u);
                }
                catch (Exception e) {
                    ExceptionWrapper.wrap(e);
                }
            };
        }
        catch (ExceptionWrapper e) {
            return e.unwrapRuntime();
        }
    }
    
    // TODO: docs
    public static <T> Supplier<T> unchecked(UncheckedSupplier<T> uncheckedSupplier) {
        try {
            return () -> {
                try {
                    return uncheckedSupplier.get();
                }
                catch (Exception e) {
                    return ExceptionWrapper.wrap(e);
                }
            };
        }
        catch (ExceptionWrapper e) {
            return e.unwrapRuntime();
        }
    }
    
    @FunctionalInterface
    public interface UncheckedRunnable {
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
    public interface UncheckedConsumer<T> {
        /**
         * Performs this operation on the given argument.
         *
         * @param t the input argument
         */
        @SuppressWarnings("squid:S00112")
        void accept(T t) throws Exception;
    }
    
    @FunctionalInterface
    public interface UncheckedBiConsumer<T, U> {
        /**
         * Performs this operation on the given arguments.
         *
         * @param t the first input argument
         * @param u the second input argument
         */
        @SuppressWarnings("squid:S00112")
        void accept(T t, U u) throws Exception;
    }
    
    public interface UncheckedSupplier<T> {
        /**
         * Gets a result.
         *
         * @return a result
         */
        @SuppressWarnings("squid:S00112")
        T get() throws Exception;
    }
}
