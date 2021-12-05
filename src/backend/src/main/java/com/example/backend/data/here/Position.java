package com.example.backend.data.here;

import com.google.gson.annotations.SerializedName;

public class Position {

    @SerializedName("lat")
    public double lat;
    @SerializedName("lng")
    public double lng;

    public Position(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public String toString(String tab) {
        return "\n" + tab + "Position{" +
                "\n" + tab + "\tlat = " + lat +
                "\n" + tab + "\tlng = " + lng +
                "\n" + tab + "\t}";
    }
}