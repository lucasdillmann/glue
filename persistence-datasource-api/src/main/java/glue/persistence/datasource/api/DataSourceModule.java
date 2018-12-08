package glue.persistence.datasource.api;

import glue.core.exception.StartupException;
import glue.core.module.ModuleLifecycle;
import org.slf4j.Logger;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * Glue {@link ModuleLifecycle} implementation for Persistence DataSource API
 *
 * <p>This class implements the Glue module API to deal with Persistence DataSource operations. The main goal
 * here is to validate if a valid implementation of {@link DataSourceProvider} is available at the classpath.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-08
 */
@Default
public class DataSourceModule implements ModuleLifecycle {

    private static final String NO_IMPLEMENTATION_FOUND_MESSAGE = "A valid implementation of the Persistence DataSource was " +
            "not found. Check if you have a valid implementation module in the classpath.";
    private static final String MULTIPLE_IMPLEMENTATIONS_FOUND_MESSAGE = "Multiple implementations of the Persistence " +
            "DataSource API was found in the classpath. Resolution don't which one to use.";

    private final Instance<DataSourceProvider> provider;
    private final Logger logger;

    /**
     * Constructor with {@link DataSourceProvider} {@link Instance} and {@link Logger} initialization
     *
     * @param provider DataSource provider instance
     * @param logger Logger
     */
    @Inject
    public DataSourceModule(final Instance<DataSourceProvider> provider, final Logger logger) {
        this.provider = provider;
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
        logger.info("Starting Persistence DataSource API");
        if (provider.isUnsatisfied())
            throw new StartupException(NO_IMPLEMENTATION_FOUND_MESSAGE);
        if (provider.isAmbiguous())
            throw new StartupException(MULTIPLE_IMPLEMENTATIONS_FOUND_MESSAGE);

        logger.info("Persistence DataSource API started using {} as the provider", provider.get().getClass().getSimpleName());
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
