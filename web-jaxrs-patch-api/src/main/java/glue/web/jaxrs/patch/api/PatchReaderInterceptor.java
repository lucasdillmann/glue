package glue.web.jaxrs.patch.api;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.PATCH;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * JAX-RS {@link ReaderInterceptor} implementation for {@link PATCH} requests
 *
 * <p>This class implements the JAX-RS {@link ReaderInterceptor} API to integrate the Glue PATCH API with the JAX-RS.
 * Main goal of this class is to intercept all HTTP PATCH requests, firing a compatible {@link PatchProcessor}
 * to later replace the request with the patched content.</p>
 *
 * <p>All resources classes with {@link PATCH} methods will be automatically intercepted by this class to handle
 * the patching operation. This class expects that the resource classes implements the {@link PatchResource}
 * interface. Any {@link PATCH} request received mapped to a resource class that don't implement the interface will
 * lead to an exception.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-23
 */
@Provider
public class PatchReaderInterceptor implements ReaderInterceptor {

    @Context
    private UriInfo uriInfo;
    @Context
    private ResourceInfo resourceInfo;
    @Context
    private ResourceContext resourceContext;

    private final PatchProcessorResolver resolver;
    private final Logger logger;

    /**
     * Constructor with {@link PatchProcessorResolver} and {@link Logger} initialization
     *
     * @param resolver PatchProcessor implementation resolver
     * @param logger Logger
     */
    @Inject
    public PatchReaderInterceptor(final PatchProcessorResolver resolver, final Logger logger) {
        this.resolver = resolver;
        this.logger = logger;
    }

    /**
     * Interceptor method wrapping calls to {@link MessageBodyReader#readFrom} method.
     * <p>
     * The parameters of the wrapped method called are available from {@code context}.
     * Implementations of this method SHOULD explicitly call {@link ReaderInterceptorContext#proceed}
     * to invoke the next interceptor in the chain, and ultimately the wrapped
     * {@link MessageBodyReader#readFrom} method.
     *
     * @param requestContext invocation context.
     * @return result of next interceptor invoked or the wrapped method if last interceptor in chain.
     * @throws IOException             if an IO error arises or is thrown by the wrapped
     *                                 {@code MessageBodyReader.readFrom} method.
     * @throws WebApplicationException thrown by the wrapped {@code MessageBodyReader.readFrom} method.
     */
    @Override
    @PATCH
    public Object aroundReadFrom(final ReaderInterceptorContext requestContext)
            throws IOException, WebApplicationException {

        logger.debug("PATCH request received for {} using media type {}", uriInfo.getPath(), requestContext.getMediaType());

        final MediaType mediaType = requestContext.getMediaType();
        final InputStream content = requestContext.getInputStream();
        final Optional target = getTargetObject();

        final InputStream patchedObject = resolver
                .getProcessor(mediaType)
                .apply(content, target);

        requestContext.setInputStream(patchedObject);

        logger.debug("PATCH requested handled successfully");
        return requestContext.proceed();
    }

    /**
     * Identifies and returns the target patch object from routed resource class
     *
     * @return Identified target object for the patch operation
     */
    private Optional getTargetObject() {
        final Class<?> resourceClass = resourceInfo.getResourceClass();
        final Object resourceInstance = resourceContext.getResource(resourceClass);

        if (!(resourceInstance instanceof PatchResource)) {
            final String exceptionMessage = new StringBuilder()
                    .append("Resource class ")
                    .append(resourceClass.getName())
                    .append(" has PATCH annotation on method ")
                    .append(resourceInfo.getResourceMethod().getName())
                    .append(" but don't implements interface ")
                    .append(PatchResource.class.getName())
                    .append(". PATCH requests using Glue PATCH API requires that the resource implements ")
                    .append("the patch interface to work.")
                    .toString();
            final Response.Status exceptionStatus = Response.Status.INTERNAL_SERVER_ERROR;

            throw new PatchException(exceptionMessage, exceptionStatus);
        }

        final PatchResource patchResource = (PatchResource) resourceInstance;
        return patchResource.getPatchTargetObject(resourceInfo, uriInfo);
    }
}
