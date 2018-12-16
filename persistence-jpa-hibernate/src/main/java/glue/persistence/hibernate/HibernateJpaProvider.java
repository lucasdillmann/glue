package glue.persistence.hibernate;

import glue.persistence.hibernate.exception.GluePersistenceException;
import glue.persistence.jpa.api.JpaProvider;
import org.slf4j.Logger;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceProviderResolverHolder;
import java.util.List;

/**
 * Hibernate implementation for {@link JpaProvider}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-12
 */
@Default
@Singleton
public class HibernateJpaProvider implements JpaProvider {

    private final HibernatePersistenceUnit persistenceUnit;
    private final Logger logger;

    /**
     * Constructor with {@link HibernatePersistenceUnit} and {@link Logger} initialization
     *
     * @param persistenceUnit Persistence Unit
     * @param logger Logger
     */
    @Inject
    public HibernateJpaProvider(final HibernatePersistenceUnit persistenceUnit, final Logger logger) {
        this.persistenceUnit = persistenceUnit;
        this.logger = logger;
    }

    /**
     * Creates and returns an {@link EntityManagerFactory} instance
     *
     * @return Entity manager factory
     */
    @Override
    public EntityManagerFactory buildFactory() {
        logger.info("Creating a container entity manager factory using Hibernate ORM implementation for JPA");

        final List<PersistenceProvider> providers = PersistenceProviderResolverHolder
                .getPersistenceProviderResolver()
                .getPersistenceProviders();

        if (providers == null || providers.isEmpty())
            throw new GluePersistenceException("Hibernate JPA provider not found. Something is wrong with Glue APIs, please report this error.");

        return providers
                .get(0)
                .createContainerEntityManagerFactory(persistenceUnit, persistenceUnit.getProperties());
    }
}
