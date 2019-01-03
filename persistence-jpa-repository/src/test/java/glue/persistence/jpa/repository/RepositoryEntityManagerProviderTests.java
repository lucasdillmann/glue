package glue.persistence.jpa.repository;

import glue.persistence.jpa.api.EntityManagerProvider;
import glue.persistence.jpa.api.PersistenceContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Test cases for {@link RepositoryEntityManagerProvider}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-27
 */
@RunWith(MockitoJUnitRunner.class)
public class RepositoryEntityManagerProviderTests {

    @Mock
    private PersistenceContext context;
    @Mock
    private EntityManagerFactory factory;
    @Mock
    private Logger logger;

    private EntityManagerProvider provider;

    @Before
    public void setup() {
        doReturn(factory).when(context).getFactory();
        this.provider = new EntityManagerProvider(context, logger);
    }

    @Test
    public void shouldProduceEntityManager() {
        // scenario
        final EntityManager entityManager = mock(EntityManager.class);
        doReturn(entityManager).when(factory).createEntityManager();

        // execution
        final EntityManager producedEntityManager = provider.entityManager();

        // validation
        assertNotNull(producedEntityManager);
        assertThat(producedEntityManager, is(entityManager));
    }
}
