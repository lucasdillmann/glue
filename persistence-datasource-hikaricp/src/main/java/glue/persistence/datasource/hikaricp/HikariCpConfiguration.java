package glue.persistence.datasource.hikaricp;

import glue.config.api.annotation.ConfigurationInterface;
import glue.config.api.annotation.ConfigurationProperty;

import java.util.Optional;

/**
 * HikariCP configuration interface
 *
 * <p>This interface implements the Glue Configurtion API, allowing simple access to HikariCP configuration values.</p>
 *
 * <p>All configurations available here uses the same configuration names that HikariCP uses. Check HikariCP docs
 * for more information about what every one do.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-08
 */
@ConfigurationInterface(prefix = "glue.persistence.datasource.")
public interface HikariCpConfiguration {

    @ConfigurationProperty(key = "poolName", defaultValue = "glue-hikaricp-pool")
    Optional<String> getPoolName();

    Optional<Boolean> isAutoCommit();
    Optional<Long> getConnectionTimeout();
    Optional<Long> getIdleTimeout();
    Optional<Long> getMaxLifetime();
    Optional<String> getConnectionTestQuery();
    Optional<Integer> getMinimumIdle();
    Optional<Integer> getMaximumPoolSize();
    Optional<Long> getInitializationFailTimeout();
    Optional<Boolean> isIsolateInternalQueries();
    Optional<Boolean> isAllowPoolSuspension();
    Optional<Boolean> isReadOnly();
    Optional<Boolean> isRegisterMbeans();
    Optional<String> getCatalog();
    Optional<String> getConnectionInitSql();
    Optional<String> getTransactionIsolation();
    Optional<Long> getValidationTimeout();
    Optional<Long> getLeakDetectionThreshold();
}
