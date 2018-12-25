package glue.web.jaxrs.patch.api;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.util.Optional;

/**
 * Glue JAX-RS Patch processor definition interface
 *
 * <p>This interface provides to Glue JAX-RS Patch API methods to allow detection and invocation of all patch
 * processor implementations.</p>
 *
 * <p>In another words, this interface is used to detect compatible processor and invoke them whenever a
 * {@link javax.ws.rs.PATCH} request is received using JAX-RS an will be handled by this API.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-23
 */
public interface PatchProcessor {

    /**
     * Checks if given {@link MediaType} is compatible with the processor
     *
     * @param mediaType Media type
     * @return Result of the query. {@code true} means that this processor is able to handle requests of the given
     * media type.
     */
    boolean isCompatible(MediaType mediaType);

    /**
     * Applies the PATCH operation under the target object using instructions from the input stream
     *
     * @param input InputStream with patch operations to be applied
     * @param target Target object for the operations
     * @return Stream with patched object
     */
    InputStream apply(InputStream input, Optional target) throws WebApplicationException;

}
