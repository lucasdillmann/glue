package glue.web.jaxrs.patch.api;

import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.UriInfo;
import java.util.Optional;

/**
 * JAX-RS Patch resource interface definition
 *
 * <p>This interface defines the required method that any JAX-RS resource class should implement to be compatible
 * with Glue JAX-RS Patch API. The method defined by this interface works as a simple access point for the object
 * that will be patched.</p>
 *
 * <p>In another words, all resources that uses {@link javax.ws.rs.PATCH} annotated methods should also implement
 * this interface to allow Glue PATCH API retrieve the object that needs to be patched. After detected using
 * {@link #getPatchTargetObject(ResourceInfo, UriInfo)}, API will forward the object to the actual patching
 * mechanism. After completed, patched object will be replaced as the new body of the request.</p>
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-23
 */
public interface PatchResource {

    /**
     * Returns the object to be patched
     *
     * <p>This method is called by Glue PATCH API to retrieve the target object of a {@link javax.ws.rs.PATCH}
     * request.</p>
     *
     * @param resourceInfo Resource details
     * @param uriInfo URI details
     * @return Object to be used for patching
     */
    Optional<?> getPatchTargetObject(ResourceInfo resourceInfo, UriInfo uriInfo);

}
