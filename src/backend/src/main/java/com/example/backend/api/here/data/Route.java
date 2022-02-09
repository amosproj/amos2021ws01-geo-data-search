package com.example.backend.api.here.data;

import com.example.backend.api.here.HereApiElement;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Route implements HereApiElement {

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
                "\n" + tab + "\tsections = " + createListAsString(sections, tab + "\t\t") +
                "\n" + tab + "\t}";
    }

    private String createListAsString(List<Section> sections, String tab) {
        if (sections != null && !sections.isEmpty()) {
            StringBuilder returnString = new StringBuilder();
            for (Section section : sections) {
                returnString.append(section.toString(tab));
                returnString.append("\n");
            }
            return returnString.toString();
        } else {
            return "null";
        }
    }

    public List<Place> getAlLChargingStations() {
        List<Place> allChargingStations = new ArrayList<>();
        for (Section section : sections) {
            if (section != null && section.departure != null && section.departure.place != null) {
                if (section.departure.place.type.equals("chargingStation")) {
                    section.departure.place.setPolyline(section.polyline);
                    allChargingStations.add(section.departure.place);
                }
            }
        }
        return allChargingStations;
    }
}