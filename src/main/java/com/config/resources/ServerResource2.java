package com.config.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.toclient.ProfileData;
import com.twitAPI.TwitterFunctions;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/data")
public class ServerResource2 {
    TwitterFunctions tf;

    public ServerResource2() {
        tf = new TwitterFunctions();
    }

    @GET
    @Timed
    @Path("get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResponse() {
        String ret = (new ProfileData("Name", null, null, 0, false, null)).serialize();

        Response response = Response.ok(ret).build();
        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        return response;
    }

    @POST
    @Timed
    @Path("post/{org_handle}/{controversial}/{profane}/{prospects}/{interest}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateName(
            @PathParam("org_handle") String org_handle,
            @PathParam("controversial") String controversial,
            @PathParam("profane") String profane,
            @PathParam("prospects") String prospects,
            @PathParam("interest") String interest) {

        boolean b_controversial = Boolean.parseBoolean(controversial);
        boolean b_profane = Boolean.parseBoolean(profane);
        boolean b_prospects = Boolean.parseBoolean(prospects);
        boolean b_interest = Boolean.parseBoolean(interest);

        //String ret = "controversial=" + controversial + "  profane=" +
        //profane + "  prospects=" + prospects + "  interest=" + interest;

        // list of schools must be inputted
        List<ProfileData> profiles = new ArrayList<ProfileData>(200);
        List<String> hsh;

        try {
            hsh = tf.highSchoolOnlyHandles(org_handle, 200);
            System.out.println("HSH SIZE: " + hsh.size());

            for (int i = 0; i < hsh.size(); i++) {
                ProfileData pd = new ProfileData();
                pd.name = tf.getName(hsh.get(i));
                pd.posts = tf.getFlaggedTweets(hsh.get(i));
                pd.profileImageURL = tf.getProfilePicURL(hsh.get(i));
                pd.intUniversities = tf.getProspects(hsh.get(i));
                pd.fitness = tf.percentFit(hsh.get(i));

                if (pd.posts.size() > 0)
                    pd.caution = true;
                else
                    pd.caution = false;

                profiles.add(pd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> serials = new ArrayList<String>();

        StringBuilder sb = new StringBuilder("{content:[");
        for (int i = 0; i < profiles.size(); i++) {
            if (i == profiles.size() - 1)
                sb.append(profiles.get(i).serialize());
            else
                sb.append(profiles.get(i).serialize() + ",");
        }

        sb.append("]}");

        Response response = Response.ok(sb.toString()).build();

        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        return response;
    }
}