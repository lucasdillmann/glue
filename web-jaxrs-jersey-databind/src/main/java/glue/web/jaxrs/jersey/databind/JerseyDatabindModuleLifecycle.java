package glue.web.jaxrs.jersey.databind;

import glue.core.module.ModuleLifecycle;
import glue.web.jaxrs.api.JaxRsProvider;
import org.slf4j.Logger;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

/**
 * {@link ModuleLifecycle} implementation for Jersey Databind Module
 *
 * <p>This class implements the Glue {@link ModuleLifecycle} to listen for application startup events, allowing
 * to automatically register the ObjectMapper context resolver.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-25
 */
@Default
public class JerseyDatabindModuleLifecycle implements ModuleLifecycle {

    private final Logger logger;
    private final JaxRsProvider jaxrsProvider;

    /**
     * Constructor with {@link JaxRsProvider} and {@link Logger} initialization
     *
     * @param jaxrsProvider JAX-RS provider
     * @param logger        Logger
     */
    @Inject
    public JerseyDatabindModuleLifecycle(final JaxRsProvider jaxrsProvider,
                                         final Logger logger) {
        this.logger = logger;
        this.jaxrsProvider = jaxrsProvider;
    }

    /**
     * Starts the module
     *
     * <p>This method starts the module lifecycle, firing up all the required actions to allow the features provided
     * by it to start working.</p>
     */
    @Override
    public void start() {
        jaxrsProvider.registerClass(ObjectMapperResolver.class);
        logger.info("Jackson ObjectMapper resolver started under JAX-RS context");
    }

    /**
     * Stops the module
     *
     * <p>This method stop the module lifecycle, shutting down the features provided by it.</p>
     */
    @Override
    public void stop() {
    }
}
