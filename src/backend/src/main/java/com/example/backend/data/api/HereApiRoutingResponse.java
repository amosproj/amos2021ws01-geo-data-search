package com.example.backend.data.api;

import com.example.backend.data.here.Route;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HereApiRoutingResponse {

    @SerializedName("routes")
    public List<Route> routes;

    public HereApiRoutingResponse(List<Route> routes) {
        this.routes = routes;
    }

    public String toString(String tab) {
        return "\n" + tab + "HereApiRoutingResponse{" +
                "\n" + tab + "\troutes = " + createListAsString(routes, tab + "\t\t") +
                "\n" + tab + "\t}";
    }

    private String createListAsString(List<Route> sections, String tab) {
        if (sections != null && !sections.isEmpty()) {
            StringBuilder returnString = new StringBuilder();
            for (Route section : sections) {
                returnString.append(section.toString(tab));
                returnString.append("\n");
            }
            return returnString.toString();
        } else {
            return "null";
        }
    }
}