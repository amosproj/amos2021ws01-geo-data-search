package com.example.backend.api.here.data;

import com.example.backend.api.here.HereApiElement;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Road implements HereApiElement {

    @SerializedName("name")
    public List<RoadInfo> roads;

    public Road(List<RoadInfo> roads) {
        this.roads = roads;
    }

    public String toString(String tab) {
        return "\n" + tab + "Road{" +
                "\n" + tab + "\troads = " + createListAsString(roads, tab + "\t\t") +
                "\n" + tab + "\t}";
    }

    private String createListAsString(List<RoadInfo> sections, String tab) {
        if (sections != null && !sections.isEmpty()) {
            StringBuilder returnString = new StringBuilder();
            for (RoadInfo section : sections) {
                returnString.append(section.toString(tab));
                returnString.append("\n");
            }
            return returnString.toString();
        } else {
            return "null";
        }
    }
}