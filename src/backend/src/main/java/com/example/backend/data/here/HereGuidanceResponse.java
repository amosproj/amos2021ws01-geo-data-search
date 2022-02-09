package com.example.backend.data.here;

import java.util.List;

public class HereGuidanceResponse extends HereApiRoutingResponse {

    public HereGuidanceResponse(List<Route> routes) {
        super(routes);
    }

    public String toString(String tab) {
        return "\n" + tab + "HereGuidanceResponse{" +
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