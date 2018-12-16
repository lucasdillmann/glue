package glue.web.jaxrs.api;

import glue.core.module.ModuleLifecycle;
import org.slf4j.Logger;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

/**
 * Glue {@link ModuleLifecycle} implementation for JAX-RS
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-16
 */
@Default
public class JaxRsModule implements ModuleLifecycle {

    private final JaxRsInstaller installer;
    private final Logger logger;

    /**
     * Constructor with {@link JaxRsInstaller} and {@link Logger} initialization
     *
     * @param installer JAX-RS installer
     * @param logger Logger
     */
    @Inject
    public JaxRsModule(final JaxRsInstaller installer, final Logger logger) {
        this.installer = installer;
        this.logger = logger;
    }

    /**
     * Starts the module
     *
     * <p>This method starts the module lifecycle, firing up all the required actions to allow the features provided
     * by it to start working.</p>
     */
    @Override
    public void start() {
        logger.info("Starting JAX-RS API");
        installer.install();
    }

    /**
     * Stops the module
     *
     * <p>This method stop the module lifecycle, shutting down the features provided by it.</p>
     */
    @Override
    public void stop() {
        logger.info("Stopping JAX-RS API");
        installer.uninstall();
    }
}
