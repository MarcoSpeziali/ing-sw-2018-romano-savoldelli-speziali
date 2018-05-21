package it.polimi.ingsw.utils.streams;

public class StreamExceptionWrapper extends RuntimeException {

    private static final long serialVersionUID = 4262663147040028724L;

    private StreamExceptionWrapper(Exception exceptionToWrap) {
        super(exceptionToWrap);
    }

    /**
     * Wraps the generated exception.
     * @param exceptionToWrap the exception to wrap
     */
    public static <T> T wrap(Exception exceptionToWrap) {
        throw new StreamExceptionWrapper(exceptionToWrap);
    }

    /**
     * Tries to unwrap the provided exception.
     * @param exceptionClass the exception to unwrap
     * @param <T> the exception to unwrap
     * @throws T if the exception has been successfully unwrapped
     */
    public <T extends Throwable> StreamExceptionWrapper tryUnwrap(Class<T> exceptionClass) throws T {
        if (this.getCause().getClass().equals(exceptionClass)) {
            //noinspection unchecked
            throw (T) this.getCause();
        }

        return this;
    }

    /**
     * Tries to unwrap the provided exception, if fails a {@link RuntimeException} is thrown.
     * @param exceptionClass the exception to unwrap
     * @param <T> the exception to unwrap
     * @param <K> the fake return value
     * @throws T if the exception has been successfully unwrapped
     */
    @SuppressWarnings("squid:S00112")
    public <T extends Throwable, K> K tryFinalUnwrap(Class<T> exceptionClass) throws T {
        if (this.getCause().getClass().equals(exceptionClass)) {
            //noinspection unchecked
            throw (T) this.getCause();
        }

        throw new RuntimeException(this.getCause());
    }
}
