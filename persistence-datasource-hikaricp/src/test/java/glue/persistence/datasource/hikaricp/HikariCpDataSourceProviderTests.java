package glue.persistence.datasource.hikaricp;

import com.zaxxer.hikari.HikariDataSource;
import glue.persistence.datasource.api.ConnectionProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import javax.sql.DataSource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

/**
 * Test cases for {@link HikariCpDataSourceProvider}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-08
 */
@RunWith(MockitoJUnitRunner.class)
public class HikariCpDataSourceProviderTests {

    @Mock
    private HikariCpFactory factory;
    @Mock
    private HikariDataSource dataSource;
    @Mock
    private Logger logger;

    private HikariCpDataSourceProvider provider;

    @Before
    public void setup() {
        doReturn(dataSource).when(factory).build(any(ConnectionProperties.class));
        this.provider = new HikariCpDataSourceProvider(factory, logger);
    }

    @Test
    public void shouldProduceDataSourceInstances() {
        // scenario
        final ConnectionProperties connectionProperties = new ConnectionProperties(null, null, null, null, null);

        // execution
        final DataSource actualDataSource = provider.produce(connectionProperties);

        // validation
        assertNotNull(actualDataSource);
        assertThat(actualDataSource, is(dataSource));
    }

}
