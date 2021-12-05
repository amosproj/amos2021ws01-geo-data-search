package com.example.backend.data.here;

import com.google.gson.annotations.SerializedName;

public class RoutePoint {

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
                "\n" + tab + "\tplace = " + place.toString(tab + "\t\t") +
                "\n" + tab + "\t}";
    }
}