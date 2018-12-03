package glue.config.api.exception;

import glue.core.exception.ApplicationException;

/**
 * Glue Application exception class for configuration related problems
 *
 * <p>This exception extends the Glue {@link ApplicationException} for configuration related issues.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-02
 */
public class ConfigurationException extends ApplicationException {

    /**
     * Constructor with message exception initialization
     *
     * @param message Exception message
     */
    public ConfigurationException(final String message) {
        super(message);
    }

    /**
     * Constructor with exception message and cause initialization
     *
     * @param message Exception message
     * @param cause   Exception cause
     */
    public ConfigurationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
