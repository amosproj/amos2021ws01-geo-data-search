package com.example.backend.api.here.data;

import com.example.backend.api.here.HereApiElement;
import com.example.backend.api.here.data.Place;
import com.google.gson.annotations.SerializedName;

public class RoutePoint implements HereApiElement {

    @SerializedName("time")
    public String time;
    @SerializedName("place")
    public Place place;

    public RoutePoint(String time, Place place) {
        this.time = time;
        this.place = place;
    }

    public String toString(String tab) {
        return "\n" + tab + "RoutePoint{" +
                "\n" + tab + "\ttime = \"" + time + "\"" +
                "\n" + tab + "\tplace = " + print(place, tab) +
                "\n" + tab + "\t}";
    }

    private String print(HereApiElement element, String tab) {
        if (element != null) {
            return element.toString(tab + "\t\t");
        } else {
            return "null";
        }
    }
}