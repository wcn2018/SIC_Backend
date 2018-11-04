package com.toclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class ProfileData {
    public String name;
    public List<String> posts;
    public List<String> intUniversities;

    public ProfileData(String name, List<String> posts, List<String> intUniversities) {
        this.name = name;
        this.posts = posts;
        this.intUniversities = intUniversities;
    }

    public String serialize() {
        Gson gson = (new GsonBuilder()).serializeNulls().create();
        return gson.toJson(this);
    }
}
