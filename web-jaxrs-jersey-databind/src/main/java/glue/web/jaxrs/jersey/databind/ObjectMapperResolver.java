package glue.web.jaxrs.jersey.databind;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * JAX-RS {@link ContextResolver} implementation for {@link ObjectMapper}
 *
 * <p>This class implements the JAX-RS {@link ContextResolver} API for {@link ObjectMapper}, providing object instances
 * for JAX-RS related operations.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-23
 */
@Provider
@Produces(MediaType.WILDCARD)
@Consumes(MediaType.WILDCARD)
public class ObjectMapperResolver implements ContextResolver<ObjectMapper> {

    private final ObjectMapperFactory factory;
    private final Logger logger;

    /**
     * Constructor with {@link ObjectMapperFactory} and {@link Logger} initialization
     *
     * @param factory ObjectMapper factory
     * @param logger  Logger
     */
    @Inject
    public ObjectMapperResolver(final ObjectMapperFactory factory, final Logger logger) {
        this.factory = factory;
        this.logger = logger;
    }

    /**
     * Get a context of type {@code T} that is applicable to the supplied
     * type.
     *
     * @param type the class of object for which a context is desired
     * @return a context for the supplied type or {@code null} if a
     * context for the supplied type is not available from this provider.
     */
    @Override
    public ObjectMapper getContext(final Class<?> type) {
        logger.debug("Producing {} instance for JAX-RS context", ObjectMapper.class.getName());
        return factory.build();
    }
}
