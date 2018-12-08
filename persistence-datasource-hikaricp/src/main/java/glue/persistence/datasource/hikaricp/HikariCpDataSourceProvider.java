package glue.persistence.datasource.hikaricp;

import glue.persistence.datasource.api.ConnectionProperties;
import glue.persistence.datasource.api.DataSourceProvider;
import org.slf4j.Logger;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

/**
 * {@link DataSourceProvider} implementation for HikariCP connection pool
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-08
 */
@Default
@Singleton
public class HikariCpDataSourceProvider implements DataSourceProvider {

    private final HikariCpFactory factory;
    private final Logger logger;

    /**
     * Constructor with {@link HikariCpFactory} and {@link Logger} intialization
     *
     * @param factory HikariCP DataSource factory
     * @param logger Logger
     */
    @Inject
    public HikariCpDataSourceProvider(final HikariCpFactory factory, final Logger logger) {
        this.factory = factory;
        this.logger = logger;
    }

    /**
     * Produces a {@link DataSource} instance for the given {@link ConnectionProperties}
     *
     * @param connectionProperties Connection properties
     * @return Produced {@link DataSource}
     */
    @Override
    public DataSource produce(final ConnectionProperties connectionProperties) {
        logger.debug("Forwarding HikariCP DataSource production to internal factory");
        return factory.build(connectionProperties);
    }
}
