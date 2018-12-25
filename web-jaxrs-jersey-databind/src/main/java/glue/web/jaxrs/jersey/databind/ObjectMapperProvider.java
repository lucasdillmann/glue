package glue.web.jaxrs.jersey.databind;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 * {@link ObjectMapper} provider class for CDI injections
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-23
 */
public class ObjectMapperProvider {

    private final ObjectMapperFactory factory;
    private final Logger logger;

    /**
     * Constructor with {@link ObjectMapperFactory} and {@link Logger} initialization
     *
     * @param factory ObjectMapper factory
     * @param logger Logger
     */
    @Inject
    public ObjectMapperProvider(final ObjectMapperFactory factory, final Logger logger) {
        this.factory = factory;
        this.logger = logger;
    }

    /**
     * Produces an {@link ObjectMapper} instance
     *
     * @return ObjectMapper instance
     */
    @Produces
    public ObjectMapper objectMapper() {
        logger.debug("Producing a {} instance", ObjectMapper.class.getName());
        return factory.build();
    }
}
