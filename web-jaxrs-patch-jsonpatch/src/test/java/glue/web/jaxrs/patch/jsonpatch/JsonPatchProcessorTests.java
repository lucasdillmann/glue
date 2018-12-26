package glue.web.jaxrs.patch.jsonpatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import glue.web.jaxrs.patch.jsonpatch.artifacts.PatchEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Test cases for {@link JsonPatchProcessor}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-23
 */
@RunWith(MockitoJUnitRunner.class)
public class JsonPatchProcessorTests {

    @Mock
    private Logger logger;

    private ObjectMapper mapper;
    private JsonPatchProcessor processor;

    @Before
    public void setup() {
        this.mapper = new ObjectMapper();
        this.processor = new JsonPatchProcessor(mapper, logger);
    }

    @Test
    public void shouldAcceptRfc6902MediaType() {
        // scenario
        final MediaType simpleMediaType = mock(MediaType.class);
        doReturn("application").when(simpleMediaType).getType();
        doReturn("json-patch+json").when(simpleMediaType).getSubtype();

        final MediaType mediaTypeWithArguments = mock(MediaType.class);
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("charset", "utf-8");
        doReturn("application").when(mediaTypeWithArguments).getType();
        doReturn("json-patch+json").when(mediaTypeWithArguments).getSubtype();
        doReturn(parameters).when(mediaTypeWithArguments).getParameters();

        // execution
        final boolean supportsSimpleMediaType = processor.isCompatible(simpleMediaType);
        final boolean supportMediaTypeWithArguments = processor.isCompatible(mediaTypeWithArguments);

        // validation
        assertTrue(supportsSimpleMediaType);
        assertTrue(supportMediaTypeWithArguments);
    }

    @Test
    public void shouldApplyJsonPatchIntructions() throws IOException {
        // scenario
        final PatchEntity sourceEntity = new PatchEntity();
        sourceEntity.setId(new Random().nextLong());
        sourceEntity.setDescription(Integer.toString(new Random().nextInt()));

        final Long expectedIdValue = new Random().nextLong();
        final String expectedNameValue = Integer.toString(new Random().nextInt());
        final byte[] patchInstructions = new StringBuilder()
                .append("[")
                .append("{\"op\": \"replace\", \"path\": \"/id\", \"value\": \"").append(expectedIdValue).append("\"},")
                .append("{\"op\": \"remove\", \"path\": \"/description\"},")
                .append("{\"op\": \"add\", \"path\": \"/name\", \"value\": \"").append(expectedNameValue).append("\"}")
                .append("]")
                .toString()
                .getBytes(StandardCharsets.UTF_8);
        final InputStream input = new ByteArrayInputStream(patchInstructions);

        // execution
        final InputStream patchedValue = processor.apply(input, Optional.of(sourceEntity));

        // validation
        assertNotNull(patchedValue);
        final PatchEntity patchedEntity = mapper.readValue(patchedValue, PatchEntity.class);

        assertNotNull(patchedEntity);
        assertThat(patchedEntity.getId(), is(equalTo(expectedIdValue)));
        assertThat(patchedEntity.getName(), is(equalTo(expectedNameValue)));
        assertThat(patchedEntity.getDescription(), is(nullValue()));
    }
}
