package glue.persistence.hibernate;

import glue.core.util.ExceptionUtils;
import glue.persistence.datasource.api.ConnectionProperties;
import glue.persistence.datasource.api.DataSourceProvider;
import glue.persistence.hibernate.configuration.HibernateConfiguration;
import glue.persistence.hibernate.exception.GluePersistenceException;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Driver;

/**
 * Hibernate internal {@link DataSource} factory
 *
 * <p>This class uses project configuration values to produce {@link DataSource} instances for Hibernate use.
 * All produces {@link DataSource} are created using Glue DataSource API, which means that any connection pool
 * can be used that implements the Glue API.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-12
 */
@Singleton
@Default
class HibernateDataSourceFactory {

    private final DataSourceProvider provider;
    private final HibernateConfiguration configuration;
    private final Logger logger;

    /**
     * Constructor with {@link DataSourceProvider}, {@link HibernateConfiguration} and {@link Logger} initialization
     *
     * @param provider DataSource provider
     * @param configuration Configuration values
     * @param logger Logger
     */
    @Inject
    public HibernateDataSourceFactory(final DataSourceProvider provider,
                                      final HibernateConfiguration configuration,
                                      final Logger logger) {
        this.provider = provider;
        this.configuration = configuration;
        this.logger = logger;
    }

    /**
     * Produces a {@link DataSource} using project configuration values
     *
     * <p>This method uses project configuration values and Glue DataSource API to produce a Hibernate compatible
     * instance.</p>
     *
     * @return Produced DataSource
     */
    DataSource build() {
        final ConnectionProperties connectionProperties = buildConnectionProperties();
        return provider.produce(connectionProperties);
    }

    /**
     * Validates if the configuration values are valid. This method is called using CDI {@link PostConstruct}.
     */
    @PostConstruct
    void validateConfiguration() {
        logger.debug("Validating configuration");
        final String driverClass = configuration.getDriverClass();
        try {
            final Class<?> configuredClass = Class.forName(driverClass);
            if (configuredClass == null || !Driver.class.isAssignableFrom(configuredClass))
                throw new GluePersistenceException("Driver class isn't a valid java.sql.Driver implementation: " + configuredClass);

        } catch (final Exception ex) {
            throw new GluePersistenceException("Configuration for persistence driver class is invalid", ex);
        }
    }

    /**
     * Builds and returns an instance of {@link ConnectionProperties} using project configuration values
     *
     * @return Connection properties
     */
    private ConnectionProperties buildConnectionProperties() {
        final Class<? extends Driver> driverClass;
        try {
            driverClass = (Class<? extends Driver>) Class.forName(configuration.getDriverClass());
        } catch (final Exception ex) {
            new ExceptionUtils().rethrowAsUnchecked(ex);
            return null;
        }

        return new ConnectionProperties(
                configuration.getUrl(),
                configuration.getUsername(),
                configuration.getPassword(),
                configuration.getSchema(),
                driverClass
        );
    }
}
