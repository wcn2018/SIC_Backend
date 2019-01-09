package com.toclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class ProfileData {
    public String name;
    public List<String> posts;
    public List<String> intUniversities;
    public int fitness;
    public String profileImageURL;
    public boolean caution;

    public ProfileData() {}

    public ProfileData(String name,
                       List<String> posts,
                       List<String> intUniversities,
                       int fitness,
                       boolean caution,
                       String profileImageURL) {
        this.name = name;
        this.posts = posts;
        this.intUniversities = intUniversities;
        this.fitness = fitness;
        this.caution = caution;
        this.profileImageURL = profileImageURL;
    }

    public String serialize() {
        Gson gson = (new GsonBuilder()).serializeNulls().create();
        return gson.toJson(this);
    }

    public static void main(String[] args) {
    }
}