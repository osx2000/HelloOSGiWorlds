package com.github.osx2000.how.service.rest;

import com.github.osx2000.how.interfaces.Echo;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Component(service = Hello.class)
@Path("/hello")
public class Hello {

    @Reference(service=Echo.class,cardinality = ReferenceCardinality.MANDATORY)
    Echo echo;

    @GET
    public String getHello() {
        return "Hello!";
    }

    @GET
    @Path("/echo/{input}")
    public String getEcho(@PathParam("input") String input) {
        return echo.getEcho(input);
    }
}
