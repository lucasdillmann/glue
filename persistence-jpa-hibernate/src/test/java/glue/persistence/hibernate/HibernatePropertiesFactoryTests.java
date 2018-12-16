package glue.persistence.hibernate;

import glue.persistence.hibernate.properties.HibernatePropertiesCustomizer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import javax.enterprise.inject.Instance;
import java.util.Arrays;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Test cases for {@link HibernatePropertiesFactory}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-12
 */
@RunWith(MockitoJUnitRunner.class)
public class HibernatePropertiesFactoryTests {

    @Mock
    private Instance<HibernatePropertiesCustomizer> instance;
    @Mock
    private HibernatePropertiesCustomizer customizer;
    @Mock
    private Logger logger;

    private HibernatePropertiesFactory factory;

    @Before
    public void setup() {
        doReturn(Arrays.asList(customizer).spliterator()).when(instance).spliterator();
        this.factory = new HibernatePropertiesFactory(instance, logger);
    }

    @Test
    public void shouldProduceHibernateProperties() {
        // execution
        final Properties properties = factory.build();

        // validation
        assertNotNull(properties);
    }

    @Test
    public void shouldInvokeCustomizers() {
        // execution
        factory.build();

        // validation
        verify(customizer, times(1)).customize(any(Properties.class));
    }
}
