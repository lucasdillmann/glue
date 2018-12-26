package glue.web.jaxrs.jersey.databind;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.stream.StreamSupport;

/**
 * {@link ObjectMapper} factory class
 *
 * <p>This class is able to produce {@link ObjectMapper} instances. Any instance created with this class is
 * independent from each other, but all instances are created from a common template object previously customized
 * by all available {@link ObjectMapperCustomizer} implementations.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-23
 */
@Singleton
public class ObjectMapperFactory {

    private final ObjectMapper template;
    private final Instance<ObjectMapperCustomizer> customizers;
    private final Logger logger;

    /**
     * Constructor with {@link Logger} initialization
     *
     * @param customizers CDI {@link Instance} gateway object for {@link ObjectMapperCustomizer}
     * @param logger Logger
     */
    @Inject
    public ObjectMapperFactory(final @Any Instance<ObjectMapperCustomizer> customizers, final Logger logger) {
        this.customizers = customizers;
        this.logger = logger;
        this.template = new ObjectMapper();
    }

    /**
     * Initializes the {@link ObjectMapper} template object by enabling basic configurations and invoking
     * {@link ObjectMapperCustomizer} implementations
     */
    @PostConstruct
    void init() {
        template.registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());

        StreamSupport
                .stream(customizers.spliterator(), true)
                .forEach(customizer -> {
                    logger.debug(
                            "Invoking {} to customize {} template object",
                            customizer.getClass().getName(), ObjectMapper.class.getName()
                    );
                    customizer.customize(template);
                });
    }

    /**
     * Produces a {@link ObjectMapper} instance
     *
     * @return ObjectMapper instance
     */
    ObjectMapper build() {
        logger.debug("Producing {} instance from internal template", ObjectMapper.class.getName());
        return template.copy();
    }
}
