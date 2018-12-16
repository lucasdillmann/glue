package glue.persistence.hibernate.exception;

import glue.core.exception.ApplicationException;

/**
 * Glue exception for persistence related errors
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-12
 */
public class GluePersistenceException extends ApplicationException {

    /**
     * Constructor with message exception initialization
     *
     * @param message Exception message
     */
    public GluePersistenceException(String message) {
        super(message);
    }

    /**
     * Constructor with exception message and cause initialization
     *
     * @param message Exception message
     * @param cause   Exception cause
     */
    public GluePersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
