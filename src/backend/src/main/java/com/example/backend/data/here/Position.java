package com.example.backend.data.here;

import com.google.gson.annotations.SerializedName;

public class Position {

    @SerializedName("lat")
    double lat;
    @SerializedName("lng")
    double lng;

    public Position(){

    }

    public Position(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "\n\t\t\tPosition{" +
                "\n\t\t\t\tlat = \"" + lat + "\"" +
                "\n\t\t\t\tlng = \"" + lng + "\"" +
                "\n\t\t\t\t}";
    }
}
