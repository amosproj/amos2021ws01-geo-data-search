package com.example.backend.data.here;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Route {

    @SerializedName("id")
    public String id;
    @SerializedName("sections")
    public List<Section> sections;

    public Route(String id, List<Section> sections) {
        this.id = id;
        this.sections = sections;
    }

    public String toString(String tab) {
        return "\n" + tab + "Route{" +
                "\n" + tab + "\tid = \"" + id + "\"" +
                "\n" + tab + "\tsections = " + sections.get(0).toString(tab + "\t\t") +
                "\n" + tab + "\t}";
    }
}