package glue.core;

import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;

/**
 * Glue application context class
 *
 * <p>This class provides access to Glue application context, allowing control over the application lifecycle and
 * access to useful information (like the application startup class).</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-12
 */
@Singleton
public final class GlueApplicationContext {

    private final Logger logger;
    private final Instance<LifecycleController> lifecycleController;
    private Class<?> startupClass;

    /**
     * Constructor with {@link Logger} and {@link LifecycleController} initialization
     *
     * @param logger              Logger
     * @param lifecycleController Lifecycle controller
     */
    @Inject
    public GlueApplicationContext(final Logger logger, final Instance<LifecycleController> lifecycleController) {
        this.logger = logger;
        this.lifecycleController = lifecycleController;
    }

    /**
     * Initializes the context by welcoming the user using log
     */
    @PostConstruct
    private void init() {
        logger.info("Welcome to Glue");
    }

    public Class<?> getStartupClass() {
        return startupClass;
    }

    void setStartupClass(final Class<?> startupClass) {
        Objects.requireNonNull(startupClass);
        logger.debug("Application startup class set to {}", startupClass.getName());
        this.startupClass = startupClass;
    }

    /**
     * Shutdown the application lifecycle
     *
     * <p>This method invokes the application shutdown by stopping its lifecycle. When called all modules will be
     * signaled to stop their jobs and then JVM will be terminated.</p>
     */
    public void shutdown() {
        lifecycleController.get().stop();
    }

    /**
     * Starts the application lifecycle
     *
     * <p>This method starts the application by starting its lifecycle. All modules will be signaled to start their
     * jobs.</p>
     */
    void start() {
        logger.debug("Starting application lifecycle");
        lifecycleController.get().start();
    }
}
