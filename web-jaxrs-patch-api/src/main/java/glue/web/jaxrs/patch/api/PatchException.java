package glue.web.jaxrs.patch.api;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * JAX-RS {@link WebApplicationException} extension for {@link javax.ws.rs.PATCH} related operations
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-23
 */
public class PatchException extends WebApplicationException {

    /**
     * Construct a new instance with a supplied message and HTTP status code.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @since 2.0
     */
    public PatchException(final String message) {
        this(message, Response.Status.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * Construct a new instance with a supplied message and HTTP status code.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param status  the HTTP status code that will be returned to the client.
     * @since 2.0
     */
    public PatchException(final String message, final Response.Status status) {
        super(message, status);
    }

    /**
     * Construct a new instance with the supplied message, root cause and response.
     *
     * @param message  the detail message (which is saved for later retrieval
     *                 by the {@link #getMessage()} method).
     * @param cause    the underlying cause of the exception.
     * @param status  the HTTP status code that will be returned to the client.
     * @since 2.0
     */
    public PatchException(final String message, final Response.Status status, final Throwable cause) {
        super(message, cause, status);
    }
}
