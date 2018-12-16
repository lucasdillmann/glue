package glue.persistence.hibernate;

import glue.persistence.datasource.api.ConnectionProperties;
import glue.persistence.datasource.api.DataSourceProvider;
import glue.persistence.hibernate.configuration.HibernateConfiguration;
import glue.persistence.hibernate.exception.GluePersistenceException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import javax.sql.DataSource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Test cases for {@link HibernateDataSourceFactory}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-12
 */
@RunWith(MockitoJUnitRunner.class)
public class HibernateDataSourceFactoryTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private DataSourceProvider provider;
    @Mock
    private HibernateConfiguration configuration;
    @Mock
    private Logger logger;

    private HibernateDataSourceFactory factory;

    @Before
    public void setup() {
        this.factory = new HibernateDataSourceFactory(provider, configuration, logger);
    }

    @Test
    public void shouldProduceDataSource() {
        // scenario
        final DataSource expectedDataSource = mock(DataSource.class);
        doReturn(expectedDataSource).when(provider).produce(any(ConnectionProperties.class));
        doReturn(org.h2.Driver.class.getName()).when(configuration).getDriverClass();

        // execution
        final DataSource actualDataSource = factory.build();

        // validation
        assertThat(actualDataSource, is(notNullValue()));
    }

    @Test
    public void shouldThrowExceptionWhenClassIsntADriver() {
        // scenario
        doReturn(getClass().getName()).when(configuration).getDriverClass();
        expectedException.expect(GluePersistenceException.class);

        // execution
        factory.validateConfiguration();
    }
}
