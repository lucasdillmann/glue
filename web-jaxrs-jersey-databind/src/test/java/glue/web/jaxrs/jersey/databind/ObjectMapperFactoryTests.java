package glue.web.jaxrs.jersey.databind;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import javax.enterprise.inject.Instance;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Test cases for {@link ObjectMapperFactory}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2012-12-23
 */
@RunWith(MockitoJUnitRunner.class)
public class ObjectMapperFactoryTests {

    @Mock
    private Logger logger;
    @Mock
    private ObjectMapperCustomizer customizer;
    @Mock
    private Instance<ObjectMapperCustomizer> instance;

    private ObjectMapperFactory factory;

    @Before
    public void setup() {
        doReturn(Arrays.asList(customizer).spliterator()).when(instance).spliterator();
        this.factory = new ObjectMapperFactory(instance, logger);
    }

    @Test
    public void shouldBuildObjectMapperInstances() {
        // execution
        final ObjectMapper objectMapper = factory.build();

        // validation
        assertNotNull(objectMapper);
    }

    @Test
    public void shouldAlwaysReturnNewObjectMapperInstance() {
        // execution
        final ObjectMapper instance1 = factory.build();
        final ObjectMapper instance2 = factory.build();

        // validation
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertThat(instance1, is(not(instance2)));
    }

    @Test
    public void shouldCallCustomizers() {
        // execution
        factory.init();

        // validation
        verify(customizer, times(1)).customize(any(ObjectMapper.class));
    }
}
