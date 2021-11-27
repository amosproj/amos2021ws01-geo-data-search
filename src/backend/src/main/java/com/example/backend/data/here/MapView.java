package com.example.backend.data.here;

import com.google.gson.annotations.SerializedName;

public class MapView {

    @SerializedName("north")
    double north;
    @SerializedName("east")
    double east;
    @SerializedName("south")
    double south;
    @SerializedName("west")
    double west;

    public MapView() {

    }

    public MapView(double north, double east, double south, double west) {
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
    }

    @Override
    public String toString() {
        return "\n\t\t\tMapView{" +
                "\n\t\t\t\tnorth = \"" + north + "\"" +
                "\n\t\t\t\teast = \"" + east + "\"" +
                "\n\t\t\t\tsouth = \"" + south + "\"" +
                "\n\t\t\t\twest = \"" + west + "\"" +
                "\n\t\t\t\t}";
    }
}