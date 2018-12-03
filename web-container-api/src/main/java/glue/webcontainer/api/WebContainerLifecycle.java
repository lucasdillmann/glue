package glue.webcontainer.api;

import glue.core.module.ModuleLifecycle;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * {@link ModuleLifecycle} implementation for Web Container API module
 *
 * <p>This class controls the Web Container API module lifecycle. The startup and shutdown events are
 * delegated to {@link WebContainer} implementation available at the classpath.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-11-26
 */
@Singleton
public class WebContainerLifecycle implements ModuleLifecycle {

    private final Logger logger;
    private final WebContainer webContainer;

    /**
     * Default constructor with {@link WebContainer} and {@link Logger} initialization
     *
     * @param webContainer WebContainer to be controlled by this lifecycle
     * @param logger Logger instance
     */
    @Inject
    public WebContainerLifecycle(WebContainer webContainer, Logger logger) {
        this.logger = logger;
        this.webContainer = webContainer;
    }

    /**
     * Starts the Web Container
     *
     * <p>This method starts the Web Container with the Glue lifecycle API.</p>
     */
    @Override
    public void start() {
        logger.info("Starting {} using {}", WebContainer.class.getSimpleName(), webContainer.getClass().getSimpleName());
        webContainer.start();
    }

    /**
     * Stops the Web Container
     *
     * <p>This method stops the Web Container with the Glue lifecycle API</p>
     */
    @Override
    public void stop() {
        logger.info("Stopping {} using {}", WebContainer.class.getSimpleName(), webContainer.getClass().getSimpleName());
        webContainer.stop();
    }
}
