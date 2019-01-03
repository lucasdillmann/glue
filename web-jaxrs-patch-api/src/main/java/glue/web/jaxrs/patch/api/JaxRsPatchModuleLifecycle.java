package glue.web.jaxrs.patch.api;

import glue.core.module.ModuleLifecycle;
import glue.web.jaxrs.api.JaxRsProvider;
import org.slf4j.Logger;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

/**
 * {@link ModuleLifecycle} implementation for JAX-RS PATCH API
 *
 * <p>This class implements the Glue {@link ModuleLifecycle} to listen for application startup events, allowing
 * to automatically register the PATCH interceptor under JAX-RS context using {@link JaxRsProvider}.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-25
 */
@Default
public class JaxRsPatchModuleLifecycle implements ModuleLifecycle {

    private final Logger logger;
    private final JaxRsProvider provider;

    /**
     * Constructor with {@link JaxRsProvider} and {@link Logger} initialization
     *
     * @param provider JAX-RS provider
     * @param logger Logger
     */
    @Inject
    public JaxRsPatchModuleLifecycle(final JaxRsProvider provider,
                                     final Logger logger) {
        this.logger = logger;
        this.provider = provider;
    }

    /**
     * Starts the module
     *
     * <p>This method starts the module lifecycle, firing up all the required actions to allow the features provided
     * by it to start working.</p>
     */
    @Override
    public void start() {
        provider.registerClass(PatchReaderInterceptor.class);
        logger.info("PATCH interceptor started under JAX-RS context");
    }

    /**
     * Stops the module
     *
     * <p>This method stop the module lifecycle, shutting down the features provided by it.</p>
     */
    @Override
    public void stop() {
        // NO-OP
    }
}
