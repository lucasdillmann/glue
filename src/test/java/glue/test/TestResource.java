package glue.test;

import glue.core.GlueApplicationContext;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/test")
public class TestResource {

    private class TestObject {
        private UUID id;
        private String description;
        private String name;

        public UUID getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public String getName() {
            return name;
        }
    }

    @Inject
    private GlueApplicationContext applicationContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response sayHello() {
        final TestObject object = new TestObject();
        object.id = UUID.randomUUID();
        object.description = "Hello from Glue @ " + applicationContext.getStartupClass().getSimpleName();

        return Response.ok(object).build();
    }

}
