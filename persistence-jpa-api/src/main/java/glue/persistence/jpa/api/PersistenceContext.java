package glue.persistence.jpa.api;

import glue.core.exception.StartupException;
import org.slf4j.Logger;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;

/**
 * Persitence context holder class
 *
 * <p>This class holds current persistence context related objects, like {@link EntityManagerFactory} instance.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-07
 */
@Singleton
public class PersistenceContext {

    private static final String NO_JPA_PROVIDER = "JPA provider not found in classpath. Please check if your project has a JPA implementation module.";

    private final EntityManagerFactory factory;
    private final Logger logger;

    /**
     * Constructor with {@link JpaProvider} and {@link Logger} initialization
     *
     * @param provider JPA provider
     * @param logger Logger
     */
    @Inject
    public PersistenceContext(final Instance<JpaProvider> provider,
                              final Logger logger) {

        logger.info("Starting JPA Persistence API");
        validateProvider(provider);

        final JpaProvider providerInstance = provider.get();
        this.factory = providerInstance.buildFactory();
        this.logger = logger;

        logger.info("JPA persistence API context started using {} as JPA provider", providerInstance.getClass().getSimpleName());
    }

    /**
     * Validates if the provided provider is a valid instance
     *
     * @param provider JPA provider to be validated
     */
    private void validateProvider(final Instance<JpaProvider> provider) {
        if (provider.isUnsatisfied())
            throw new StartupException(NO_JPA_PROVIDER);
    }

    /**
     * Returns current {@link EntityManagerFactory} instance
     *
     * @return Entity manager factory
     */
    public EntityManagerFactory getFactory() {
        return factory;
    }
}
