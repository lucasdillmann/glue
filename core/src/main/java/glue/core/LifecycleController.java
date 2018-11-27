package glue.core;

import glue.core.exception.StartupException;
import glue.core.module.ModuleLifecycle;
import glue.core.module.Priority;
import org.slf4j.Logger;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Application lifecycle controller class
 *
 * <p>This class manages the application lifecycle, starting and stopping the modules when necessary. All modules
 * are detected using {@link ModuleLifecycle} interface.</p>
 *
 * <p>All of the actions done by this class are executed automatically. The startup of the application is done when the
 * IoC from Glue was started and a instance from this class is created. The shutdown is executed using a JVM shutdown
 * listener. Whenever the JVM receives the SIGTERM signal from the OS this class starts the shutdown sequence,
 * informing all modules that they need to stop.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-11-20
 */
@Singleton
class LifecycleController {

    private static final String NO_LOG_PROVIDER_ERROR = "SLF4J Logger provider not found in classpath. Please check if your project has a log module.";
    private static final String NO_MODULES_ERROR = "No Glue modules found at classpath. We can't start the application when there's nothing to start.";

    private final List<ModuleLifecycle> modules;
    private final Logger logger;
    private final ShutdownListener jvmListener;

    /**
     * Constructor with modules, log and JVM listener initialization
     *
     * @param modules Application modules available in the classpath
     * @param logger Logger
     * @param jvmListener JVM shutdown listener
     */
    @Inject
    public LifecycleController(@Any Instance<ModuleLifecycle> modules,
                               Instance<Logger> logger,
                               Instance<ShutdownListener> jvmListener) {
        validateLogger(logger);
        validateModules(modules);

        this.modules = StreamSupport.stream(modules.spliterator(), false).collect(Collectors.toList());
        this.logger = logger.get();
        this.jvmListener = jvmListener.get();
    }

    /**
     * Validates if the provided logger is a valid instance
     *
     * @param logger Logger to be validated
     */
    private void validateLogger(final Instance<Logger> logger) {
        if (logger.isUnsatisfied())
            throw new StartupException(NO_LOG_PROVIDER_ERROR);
    }

    /**
     * Validates if the detected modules are valid
     *
     * @param modules Modules to be validated
     */
    private void validateModules(final Instance<ModuleLifecycle> modules) {
        if (modules.isUnsatisfied() || !modules.iterator().hasNext())
            throw new StartupException(NO_MODULES_ERROR);
    }

    /**
     * Starts the application lifecycle
     *
     * <p>This method uses the detected modules to start the application lifecycle. All of them will be notified
     * that the application is starting and they need to do whatever they need to start handling requests.</p>
     */
    void start() {
        logger.info("Welcome to Glue. Starting up application...");
        jvmListener.start(this::stop);

        getSortedModules().forEach(ModuleLifecycle::start);
        logger.info("Glue started");
    }

    /**
     * Stops the application lifecycle
     *
     * <p>This method uses the detected modules and inform they that the application is shutting down. This allow
     * the soft shutdown principle, where all the previous started modules can nicely stop they jobs (close connections,
     * stop threads and others).</p>
     */
    void stop() {
        logger.info("Glue is shutting down");
        jvmListener.stop();
        getSortedModules().forEach(ModuleLifecycle::stop);

        logger.info("Good bye");
        shutdownJvm();
    }

    /**
     * Stops the JVM
     */
    void shutdownJvm() {
        logger.debug("Shutting down JVM");
        System.exit(0);
    }

    /**
     * Create and returns a sorted stream from the detected modules using the priority to do the sorting
     *
     * @return Sorted stream from the detected modules
     */
    private Stream<ModuleLifecycle> getSortedModules() {
        return modules
                .stream()
                .sorted(Comparator.comparing(
                        module -> Optional.ofNullable(module.getStopPriority()).orElse(Priority.REGULAR).asInteger()
                ));
    }
}
