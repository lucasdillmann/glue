package glue.persistence.datasource.hikaricp;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import glue.persistence.datasource.api.ConnectionProperties;
import org.slf4j.Logger;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;

/**
 * HikariCP DataSource factory
 *
 * <p>This class is able to produce HikariCP DataSource for connection pools using configurations set by the
 * project.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-08
 */
@Default
@Singleton
class HikariCpFactory {

    private final HikariCpConfiguration configuration;
    private final Logger logger;

    /**
     * Constructor with {@link HikariCpConfiguration} and {@link Logger} initialization
     *
     * @param configuration HikariCP configuration
     * @param logger Logger
     */
    @Inject
    public HikariCpFactory(final HikariCpConfiguration configuration, final Logger logger) {
        this.configuration = configuration;
        this.logger = logger;
    }

    /**
     * Produces {@link HikariDataSource} instances
     *
     * <p>This method produces {@link HikariDataSource} instances using configurations from {@link HikariCpConfiguration}
     * and provided {@link ConnectionProperties}. When configuration conflict is detected values from
     * {@link ConnectionProperties} have higher precedence.</p>
     *
     * @param connectionProperties Connection properties
     * @return Produced Hikari DataSource
     */
    public HikariDataSource build(final ConnectionProperties connectionProperties) {
        Objects.requireNonNull(connectionProperties);

        logger.info("Producing a DataSource using HikariCP for {}", connectionProperties.getJdbcUrl());
        final HikariConfig config = buildConfig(connectionProperties);
        return new HikariDataSource(config);
    }

    /**
     * Translate configuration values from {@link ConnectionProperties} and {@link HikariCpConfiguration}
     * to a {@link HikariConfig} object
     *
     * @param connectionProperties Connection properties
     * @return HikariCP configuration properties
     */
    private HikariConfig buildConfig(final ConnectionProperties connectionProperties) {
        final HikariConfig config = new HikariConfig();

        config.setJdbcUrl(connectionProperties.getJdbcUrl());
        config.setUsername(connectionProperties.getUsername());
        config.setPassword(connectionProperties.getPassword());
        config.setSchema(connectionProperties.getSchema());
        config.setDriverClassName(connectionProperties.getDriverClass().getName());

        configuration.getPoolName().ifPresent(config::setPoolName);
        configuration.isAutoCommit().ifPresent(config::setAutoCommit);
        configuration.getIdleTimeout().ifPresent(config::setIdleTimeout);
        configuration.getConnectionTimeout().ifPresent(config::setConnectionTimeout);
        configuration.getMaxLifetime().ifPresent(config::setMaxLifetime);
        configuration.getConnectionTestQuery().ifPresent(config::setConnectionTestQuery);
        configuration.getMinimumIdle().ifPresent(config::setMinimumIdle);
        configuration.getMaximumPoolSize().ifPresent(config::setMaximumPoolSize);
        configuration.getInitializationFailTimeout().ifPresent(config::setInitializationFailTimeout);
        configuration.isIsolateInternalQueries().ifPresent(config::setIsolateInternalQueries);
        configuration.isAllowPoolSuspension().ifPresent(config::setAllowPoolSuspension);
        configuration.isReadOnly().ifPresent(config::setReadOnly);
        configuration.isRegisterMbeans().ifPresent(config::setRegisterMbeans);
        configuration.getCatalog().ifPresent(config::setCatalog);
        configuration.getConnectionInitSql().ifPresent(config::setConnectionInitSql);
        configuration.getTransactionIsolation().ifPresent(config::setTransactionIsolation);
        configuration.getValidationTimeout().ifPresent(config::setValidationTimeout);
        configuration.getLeakDetectionThreshold().ifPresent(config::setLeakDetectionThreshold);

        return config;
    }
}
