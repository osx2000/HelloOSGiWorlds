package com.github.osx2000.how.service.rest;

import com.github.osx2000.how.interfaces.App;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Component(service = HelloAppResource.class)
@Path("/helloapp")
public class HelloAppResource {

    @Reference
    App app;

    @GET
    @Path("/greetings/{language}/{greeting}")
    public String getGreeting(@PathParam("language") String language,
                              @PathParam("greeting") String greeting) {
        return app.getGreeting(language,greeting);
    }

    @POST
    @Path("/greetings/{language}/{greeting}/{translation}")
    public Response postGreeting(@PathParam("language") String language,
                                 @PathParam("greeting") String greeting,
                                 @PathParam("translation") String translation) {
        String status = app.postGreeting(language,greeting,translation);
        return Response.status(Response.Status.ACCEPTED).entity(status).build();
    }
}
