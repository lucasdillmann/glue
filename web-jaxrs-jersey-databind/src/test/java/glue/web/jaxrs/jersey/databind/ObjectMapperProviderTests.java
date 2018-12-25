package glue.web.jaxrs.jersey.databind;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Test cases for {@link ObjectMapperProvider}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-23
 */
@RunWith(MockitoJUnitRunner.class)
public class ObjectMapperProviderTests {

    @Mock
    private Logger logger;
    @Mock
    private ObjectMapperFactory factory;

    private ObjectMapperProvider provider;

    @Before
    public void setup() {
        this.provider = new ObjectMapperProvider(factory, logger);
    }

    @Test
    public void shouldProduceObjectMappers() {
        // scenario
        doReturn(new ObjectMapper()).when(factory).build();

        // execution
        final ObjectMapper objectMapper = provider.objectMapper();

        // validation
        assertNotNull(objectMapper);
        verify(factory, times(1)).build();
    }
}
