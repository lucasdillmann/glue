package glue.persistence.jpa.api;

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
import javax.persistence.EntityManagerFactory;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Test cases for {@link PersistenceContext}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-07
 */
@RunWith(MockitoJUnitRunner.class)
public class PersistenceContextTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private JpaProvider provider;
    @Mock
    private Instance<JpaProvider> instance;
    @Mock
    private Logger logger;
    @Mock
    private EntityManagerFactory factory;

    private PersistenceContext context;

    @Before
    public void setup() {
        doReturn(factory).when(provider).buildFactory();
        doReturn(provider).when(instance).get();
        doReturn(false).when(instance).isUnsatisfied();

        this.context = new PersistenceContext(instance, logger);
    }

    @Test
    public void shouldReturnEntityManagerFactory() {
        // execution
        final EntityManagerFactory actualFactory = context.getFactory();

        // validation
        assertNotNull(actualFactory);
        assertThat(actualFactory, is(factory));
        verify(provider, times(1)).buildFactory();
    }

    @Test
    public void shouldValidateProvider() {
        // scenario
        doReturn(true).when(instance).isUnsatisfied();
        expectedException.expect(StartupException.class);

        // execution
        new PersistenceContext(instance, logger);
    }

}
