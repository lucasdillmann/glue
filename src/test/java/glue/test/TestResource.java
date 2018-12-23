package glue.test;

import glue.core.GlueApplicationContext;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/test")
public class TestResource {

    @Inject
    private GlueApplicationContext applicationContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response sayHello() {
        return Response.ok("Hello from Glue @ " + applicationContext.getStartupClass().getSimpleName()).build();
    }

}
