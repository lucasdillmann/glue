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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.mockito.Mockito.*;

/**
 * Test cases for {@link PersistenceJpaApiModule}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-07
 */
@RunWith(MockitoJUnitRunner.class)
public class PersistenceJpaApiModuleTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private PersistenceContext context;
    @Mock
    private Logger logger;
    @Mock
    private EntityManagerFactory factory;
    @Mock
    private EntityManager entityManager;

    private PersistenceJpaApiModule module;

    @Before
    public void setup() {
        doReturn(factory).when(context).getFactory();
        doReturn(entityManager).when(factory).createEntityManager();

        this.module = new PersistenceJpaApiModule(context, logger);
    }

    @Test
    public void shouldValidateFactoryOnStart() {
        // scenario
        doReturn(true).when(entityManager).isOpen();

        // execution
        module.start();

        // validation
        verify(context, times(1)).getFactory();
        verify(factory, times(1)).createEntityManager();
    }

    @Test
    public void shouldValidateEntityManagerOnStart() {
        // scenario
        doReturn(true).when(entityManager).isOpen();

        // execution
        module.start();

        // validation
        verify(context, times(1)).getFactory();
        verify(factory, times(1)).createEntityManager();
        verify(entityManager, times(1)).isOpen();
        verify(entityManager, times(1)).close();
    }

    @Test
    public void shouldThrowExceptionIfEntityManagerIsInvalid() {
        // scenario
        doReturn(false).when(entityManager).isOpen();
        expectedException.expect(StartupException.class);

        // execution
        module.start();
    }

    @Test
    public void shouldCloseFactoryOnStop() {
        // execution
        module.stop();

        // validation
        verify(factory, times(1)).close();
    }

}
