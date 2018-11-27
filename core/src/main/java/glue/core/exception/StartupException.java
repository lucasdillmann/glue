package glue.core.exception;

/**
 * Glue startup exception
 *
 * <p>This class defines a common use exception for any erros found while Glue is starting up.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-11-24
 */
public class StartupException extends ApplicationException {

    /**
     * Constructor with exception message initialization
     *
     * @param message Exception message
     */
    public StartupException(String message) {
        super(message);
    }

    /**
     * Constructor with exception messange and cause initialization
     *
     * @param message Exception message
     * @param cause Exception cause
     */
    public StartupException(String message, Throwable cause) {
        super(message, cause);
    }

}
