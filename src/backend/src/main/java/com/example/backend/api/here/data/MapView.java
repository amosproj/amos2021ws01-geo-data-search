package com.example.backend.api.here.data;

import com.example.backend.api.here.HereApiElement;
import com.google.gson.annotations.SerializedName;

public class MapView implements HereApiElement {

    @SerializedName("north")
    public double north;
    @SerializedName("east")
    public double east;
    @SerializedName("south")
    public double south;
    @SerializedName("west")
    public double west;

    public MapView(double north, double east, double south, double west) {
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
    }

    public String toString(String tab) {
        return "\n" + tab + "MapView{" +
                "\n" + tab + "\tnorth = " + north +
                "\n" + tab + "\teast = " + east +
                "\n" + tab + "\tsouth = " + south +
                "\n" + tab + "\twest = " + west +
                "\n" + tab + "\t}";
    }
}