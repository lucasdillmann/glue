package glue.persistence.jpa.api;

import org.slf4j.Logger;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * CDI {@link EntityManager} provider
 *
 * <p>This class provides {@link EntityManager} instances for CDI injections. All instances are created under a request
 * scope using available {@link JpaProvider} implementation at the classpath.</p>
 *
 * <p>Please note that this class is able to produce {@link EntityManager} instances for the default persistence
 * context only.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-07
 */
public class EntityManagerProvider {

    private final PersistenceContext context;
    private final Logger logger;

    /**
     * Constructor with {@link PersistenceContext} and {@link Logger} initialization
     *
     * @param context Persistence context
     * @param logger  Logger instance
     */
    @Inject
    public EntityManagerProvider(final PersistenceContext context, final Logger logger) {
        this.context = context;
        this.logger = logger;
    }

    /**
     * Produces {@link EntityManager} instances
     *
     * <p>This method produces {@link EntityManager} instances under the scope of a request (if applicable).</p>
     *
     * @return EntityManager instance
     */
    @Produces
    public EntityManager entityManager() {
        logger.debug("Producing an EntityManager using default persistence context on thread {}", Thread.currentThread().getName());

        return context.getFactory().createEntityManager();
    }

    /**
     * Disposes a {@link EntityManager} by closing it if open
     *
     * @param entityManager Entity manager to be disposed
     */
    public void dispose(final @Disposes EntityManager entityManager) {
        if (entityManager.isOpen())
            entityManager.close();
    }
}
