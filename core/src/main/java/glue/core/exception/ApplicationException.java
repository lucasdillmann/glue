package glue.core.exception;

/**
 * Generic application-wide exception class
 *
 * <p>This class implements a generic, application-wide Glue exception suitable for all errors found during application
 * lifecycle. This class isn't intended to be used in simple, pure way; it means to be extended.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-11-20
 */
public abstract class ApplicationException extends RuntimeException {

    /**
     * Constructor with message exception initialization
     *
     * @param message Exception message
     */
    public ApplicationException(String message) {
        super(message);
    }

    /**
     * Constructor with exception message and cause initialization
     *
     * @param message Exception message
     * @param cause Exception cause
     */
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
