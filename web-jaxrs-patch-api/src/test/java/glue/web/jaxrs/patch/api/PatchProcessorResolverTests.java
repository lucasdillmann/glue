package glue.web.jaxrs.patch.api;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import javax.enterprise.inject.Instance;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

/**
 * Test cases for {@link PatchProcessorResolver}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-23
 */
@RunWith(MockitoJUnitRunner.class)
public class PatchProcessorResolverTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private Logger logger;
    @Mock
    private Instance<PatchProcessor> instance;
    @Mock
    private PatchProcessor processor1;
    @Mock
    private PatchProcessor processor2;
    @Mock
    private MediaType mediaType;

    private PatchProcessorResolver resolver;

    @Before
    public void setup() throws Exception {
        doReturn(Arrays.asList(processor1, processor2).spliterator()).when(instance).spliterator();
        doReturn("test").when(mediaType).getType();
        doReturn("media-type").when(mediaType).getSubtype();
        doReturn("test/media-type").when(mediaType).toString();
        this.resolver = new PatchProcessorResolver(instance, logger);
    }

    @Test
    public void shouldThrowExceptionWhenNoCompatibleProcessorIsFound() {
        // scenario
        expectedException.expect(PatchException.class);
        expectedException.expectMessage("No compatible PatchProcessor implementation found for media type " + mediaType.toString());

        // execution
        resolver.getProcessor(mediaType);
    }

    @Test
    public void shouldThrowExceptionWhenMultipleProcessorsIsFound() {
        // scenario
        doReturn(true).when(processor1).isCompatible(mediaType);
        doReturn(true).when(processor2).isCompatible(mediaType);
        expectedException.expect(PatchException.class);
        expectedException.expectMessage("Multiple compatible PatchProcessor found for media type " + mediaType.toString());

        // execution
        resolver.getProcessor(mediaType);
    }

    @Test
    public void shouldReturnCompatibleProcessor() {
        // scenario
        doReturn(true).when(processor1).isCompatible(mediaType);

        // execution
        final PatchProcessor processor = resolver.getProcessor(mediaType);

        // validation
        assertNotNull(processor);
        assertThat(processor, is(processor1));
    }
}
