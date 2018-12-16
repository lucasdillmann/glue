package glue.persistence.datasource.hikaricp;

import com.zaxxer.hikari.HikariDataSource;
import glue.persistence.datasource.api.ConnectionProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

/**
 * Test cases for {@link HikariCpFactory}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-08
 */
@RunWith(MockitoJUnitRunner.class)
public class HikariCpFactoryTests {

    @Mock
    private Logger logger;
    @Mock
    private HikariCpConfiguration configuration;

    private HikariCpFactory factory;

    @Before
    public void setup() {
        this.factory = new HikariCpFactory(configuration, logger);
    }

    @Test
    public void shouldProduceInstances() {
        // scenario
        doReturn(Optional.empty()).when(configuration).getPoolName();
        doReturn(Optional.empty()).when(configuration).isAutoCommit();
        doReturn(Optional.empty()).when(configuration).getConnectionTimeout();
        doReturn(Optional.empty()).when(configuration).getIdleTimeout();
        doReturn(Optional.empty()).when(configuration).getMaxLifetime();
        doReturn(Optional.empty()).when(configuration).getConnectionTestQuery();
        doReturn(Optional.empty()).when(configuration).getMinimumIdle();
        doReturn(Optional.empty()).when(configuration).getMaximumPoolSize();
        doReturn(Optional.empty()).when(configuration).getInitializationFailTimeout();
        doReturn(Optional.empty()).when(configuration).isIsolateInternalQueries();
        doReturn(Optional.empty()).when(configuration).isAllowPoolSuspension();
        doReturn(Optional.empty()).when(configuration).isReadOnly();
        doReturn(Optional.empty()).when(configuration).isRegisterMbeans();
        doReturn(Optional.empty()).when(configuration).getCatalog();
        doReturn(Optional.empty()).when(configuration).getConnectionInitSql();
        doReturn(Optional.empty()).when(configuration).getTransactionIsolation();
        doReturn(Optional.empty()).when(configuration).getValidationTimeout();
        doReturn(Optional.empty()).when(configuration).getLeakDetectionThreshold();

        final ConnectionProperties properties = new ConnectionProperties(
                "jdbc:h2:mem:test", "sa", "sa", null, org.h2.Driver.class
        );

        // execution
        final HikariDataSource producedInstance = factory.build(properties);

        // validation
        assertNotNull(producedInstance);
        assertThat(producedInstance, is(instanceOf(HikariDataSource.class)));
    }
}
