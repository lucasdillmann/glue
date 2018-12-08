package glue.persistence.datasource.api;

import glue.core.exception.StartupException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import javax.enterprise.inject.Instance;

import static org.mockito.Mockito.*;

/**
 * Test cases for {@link DataSourceModule}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-08
 */
@RunWith(MockitoJUnitRunner.class)
public class DataSourceModuleTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private Instance<DataSourceProvider> instance;
    @Mock
    private DataSourceProvider provider;
    @Mock
    private Logger logger;

    private DataSourceModule module;

    @Before
    public void setup() {
        doReturn(provider).when(instance).get();
        this.module = new DataSourceModule(instance, logger);
    }

    @Test
    public void shouldThrowExceptionWhenNoImplementationIsFound() {
        // scenario
        doReturn(true).when(instance).isUnsatisfied();
        doReturn(false).when(instance).isAmbiguous();
        expectedException.expect(StartupException.class);

        // execution
        module.start();
    }

    @Test
    public void shouldThrowExceptionWhenMultipleImplementationIsFound() {
        // scenario
        doReturn(false).when(instance).isUnsatisfied();
        doReturn(true).when(instance).isAmbiguous();
        expectedException.expect(StartupException.class);

        // execution
        module.start();
    }

    @Test
    public void shouldStartSuccessfully() {
        // scenario
        doReturn(false).when(instance).isAmbiguous();
        doReturn(false).when(instance).isUnsatisfied();

        // exeuction
        module.start();

        // validation
        verify(instance, times(1)).get();
        verify(logger, times(1)).info(
                eq("Persistence DataSource API started using {} as the provider"),
                any(Class.class)
        );
    }
}
