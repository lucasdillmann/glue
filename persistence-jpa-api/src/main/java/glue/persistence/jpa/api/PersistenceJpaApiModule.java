package glue.persistence.jpa.api;

import glue.core.exception.StartupException;
import glue.core.module.ModuleLifecycle;
import org.slf4j.Logger;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * {@link ModuleLifecycle} implementation for Persistence JPA API
 *
 * <p>This class implements the Glue {@link ModuleLifecycle}, enabling persistence in the application using JPA API.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-07
 */
@Default
public class PersistenceJpaApiModule implements ModuleLifecycle {

    private final PersistenceContext context;
    private final Logger logger;

    /**
     * Constructor with {@link PersistenceContext} and {@link Logger} initialization
     *
     * @param context Persistence context
     * @param logger  Logger instance
     */
    @Inject
    public PersistenceJpaApiModule(final PersistenceContext context,
                                   final Logger logger) {
        this.context = context;
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
        logger.info("Starting JPA persistence API");
        validateProvider();
    }

    private void validateProvider() {
        EntityManager entityManager = null;
        try {
            entityManager = context.getFactory().createEntityManager();
            if (!entityManager.isOpen())
                throw new IllegalStateException("Persistence provider created an invalid EntityManager");
        } catch (final Exception ex) {
            throw new StartupException("Persistence API failed to start", ex);
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }

    /**
     * Stops the module
     *
     * <p>This method stop the module lifecycle, shutting down the features provided by it.</p>
     */
    @Override
    public void stop() {
        logger.info("Shutting down JPA persistence API");
        context.getFactory().close();
    }
}
