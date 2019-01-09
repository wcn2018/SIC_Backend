package com.config.resources;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/post")
public class ServerResource3 {
    @JsonProperty
    private String name;

    public ServerResource3() {
        name = "hi";
    }

    @POST
    @Timed
    @Path("{name}")
    @Consumes("application/json")
    public javax.ws.rs.core.Response updateName(@PathParam("name") String name) {
        this.name = name;

        Response response = Response.status(200)
                .entity("Name updated: " + name)
                .build();

        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        return response;
    }
}
