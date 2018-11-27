package glue.core.util;

/**
 * Exception related utilities
 *
 * <p>This class hold utility methods for exceptions, allowing simple reuse of them.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-11-24
 */
public final class ExceptionUtils {

    /**
     * Converts a checked exception into unchecked
     *
     * <p>There's no such thing as checked exceptions in the JVM, only in the Java compiler. This method tricks the
     * compiler and allows the throw of a checked exception like it is unchecked without the need to decorate with
     * a {@link RuntimeException}.</p>
     *
     * @param exception Exception to be converted
     * @param <E> Generic type of the exception
     * @throws E Converted exception
     */
    public <E extends Exception> void rethrowAsUnchecked(Exception exception) throws E {
        throw (E) exception;
    }

}
