package glue.jaxrs.patch.jsonmergepatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import glue.web.jaxrs.patch.api.PatchException;
import glue.web.jaxrs.patch.api.PatchProcessor;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

/**
 * {@link PatchProcessor} implementation for RFC-7386
 *
 * <p>This class implements the Glue {@link PatchProcessor} API for
 * <a href="https://tools.ietf.org/html/rfc7386">RFC-7386</a> compatible requests, also called as JSON Merge Patch using
 * content type {@code application/merge-patch+json}.</p>
 *
 * @author Lucas Dillmann
 * @see <a href="https://tools.ietf.org/html/rfc7386">RFC-7386</a>
 * @since 1.0.0, 2018-19-23
 */
public class JsonMergePatchProcessor implements PatchProcessor {

    private final ObjectMapper objectMapper;
    private final Logger logger;

    /**
     * Constructor with {@link ObjectMapper} and {@link Logger} initialization
     *
     * @param objectMapper Jackson object mapper
     * @param logger       Logger
     */
    @Inject
    public JsonMergePatchProcessor(final ObjectMapper objectMapper, final Logger logger) {
        this.objectMapper = objectMapper;
        this.logger = logger;
    }

    /**
     * Checks if given {@link MediaType} is compatible with the processor
     *
     * @param mediaType Media type
     * @return Result of the query. {@code true} means that this processor is able to handle requests of the given
     * media type.
     */
    @Override
    public boolean isCompatible(final MediaType mediaType) {
        final String type = mediaType.getType();
        final String subType = mediaType.getSubtype();

        return "application".equals(type)
                && "merge-patch+json".equals(subType);
    }

    /**
     * Applies the PATCH operation under the target object using instructions from the input stream
     *
     * @param input  InputStream with patch operations to be applied
     * @param target Target object for the operations
     * @return Stream with patched object
     */
    @Override
    public InputStream apply(final InputStream input, final Optional target) throws WebApplicationException {

        logger.debug("Begin patching object using RFC-7386 (JSON Merge Patch)");

        if (!target.isPresent())
            throw new PatchException(
                    "Target object for PATCH operations can't be null",
                    Response.Status.INTERNAL_SERVER_ERROR
            );

        try {
            final JsonMergePatch patchInstructions = objectMapper.readValue(input, JsonMergePatch.class);
            final JsonNode targetJson = objectMapper.valueToTree(target.get());
            final JsonNode patchedJson = patchInstructions.apply(targetJson);
            final byte[] patchedBytes = objectMapper.writeValueAsBytes(patchedJson);

            logger.debug("Patch operations applied successfully using RFC-7386 (JSON Merge Patch)");
            return new ByteArrayInputStream(patchedBytes);
        } catch (final Exception ex) {
            throw new PatchException(
                    "Error appling patch operations using RFC-7386 (JSON Merge Patch)",
                    Response.Status.INTERNAL_SERVER_ERROR,
                    ex
            );
        }
    }
}
