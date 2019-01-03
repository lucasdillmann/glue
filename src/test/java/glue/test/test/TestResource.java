package glue.test.test;

import glue.web.jaxrs.patch.api.PatchMediaType;
import glue.web.jaxrs.patch.api.PatchResource;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("/test")
@Transactional
public class TestResource implements PatchResource {

    private final TestService service;
    private final Logger logger;

    @Inject
    public TestResource(final TestService service, final Logger logger) {
        this.service = service;
        this.logger = logger;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        logger.debug("GET received for all entities");
        final List<TestEntity> allEntities = service.getAll();
        return Response.ok(allEntities).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(final @PathParam("id") UUID id) {
        logger.debug("DELETE received");

        if (id == null)
            throw new NotFoundException();

        final Optional<TestEntity> entity = service.getById(id);
        if (!entity.isPresent())
            throw new NotFoundException();

        return Response.ok(entity.get()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(final TestEntity newEntity, final @Context UriInfo uriInfo) {
        logger.debug("POST received");
        if (service.exists(newEntity)) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        final TestEntity createdEntity = service.save(newEntity);
        final URI resourceLocation = uriInfo
                .getAbsolutePathBuilder()
                .path(createdEntity.getId().toString())
                .build();

        return Response
                .created(resourceLocation)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response put(final TestEntity entity, final @PathParam("id") UUID id) {
        logger.debug("PUT received");
        entity.setId(id);
        service.save(entity);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(final @PathParam("id") UUID id) {
        logger.debug("DELETE received");

        if (id == null)
            throw new NotFoundException();

        final Optional<TestEntity> toDelete = service.getById(id);
        if (!toDelete.isPresent())
            throw new NotFoundException();

        service.delete(toDelete.get());
        return Response.ok().build();
    }

    @PATCH
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON, PatchMediaType.JSON_MERGE_PATCH, PatchMediaType.JSON_PATCH})
    public Response patch(final TestEntity testEntity, final @PathParam("id") UUID id) {
        logger.debug("PATCH received");

        testEntity.setId(id);
        service.save(testEntity);
        return Response.ok().build();
    }

    @Override
    public Optional<TestEntity> getPatchTargetObject(final ResourceInfo resourceInfo, final UriInfo uriInfo) {
        final String idString = uriInfo.getPathParameters().getFirst("id");
        if (idString == null)
            throw new NotFoundException();

        final UUID id;
        try {
            id = UUID.fromString(idString);
        } catch (final Exception ex) {
            throw new BadRequestException();
        }

        return service.getById(id);
    }
}
