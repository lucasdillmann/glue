package glue.persistence.hibernate;

import glue.persistence.hibernate.exception.GluePersistenceException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceProviderResolver;
import javax.persistence.spi.PersistenceProviderResolverHolder;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PersistenceProviderResolverHolder.class)
public class HibernateJpaProviderTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private PersistenceProviderResolver resolver;
    @Mock
    private PersistenceProvider persistenceProvider;
    @Mock
    private HibernatePersistenceUnit persistenceUnit;
    @Mock
    private Logger logger;
    @Mock
    private EntityManagerFactory factory;

    private HibernateJpaProvider provider;

    @Before
    public void setup() throws Exception {
        mockStatic(PersistenceProviderResolverHolder.class);
        doReturn(resolver).when(PersistenceProviderResolverHolder.class, "getPersistenceProviderResolver");
        doReturn(Arrays.asList(persistenceProvider)).when(resolver).getPersistenceProviders();
        doReturn(factory).when(persistenceProvider).createContainerEntityManagerFactory(eq(persistenceUnit), any());
        this.provider = new HibernateJpaProvider(persistenceUnit, logger);
    }

    @Test
    public void shouldThrowExceptionWhenHibernateProviderIsntFound() {
        // scenario
        doReturn(Collections.emptyList()).when(resolver).getPersistenceProviders();
        expectedException.expect(GluePersistenceException.class);
        expectedException.expectMessage("Hibernate JPA provider not found. Something is wrong with Glue APIs, please report this error.");

        // execution
        provider.buildFactory();
    }

    @Test
    public void shouldProduceEntityManagerFactory() {
        // execution
        final EntityManagerFactory factory = provider.buildFactory();

        // validation
        assertNotNull(factory);
    }
}
