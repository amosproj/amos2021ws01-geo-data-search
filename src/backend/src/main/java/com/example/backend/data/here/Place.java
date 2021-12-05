package com.example.backend.data.here;

import com.google.gson.annotations.SerializedName;

public class Place {

    @SerializedName("type")
    public String type;
    @SerializedName("location")
    public Position location;
    @SerializedName("originalLocation")
    public Position originalLocation;

    public Place(String type, Position location, Position originalLocation) {
        this.type = type;
        this.location = location;
        this.originalLocation = originalLocation;
    }

    public String toString(String tab) {
        return "\n" + tab + "Place{" +
                "\n" + tab + "\ttype = \"" + type + "\"" +
                "\n" + tab + "\tlocation = " + location.toString(tab + "\t\t") +
                "\n" + tab + "\toriginalLocation = " + originalLocation.toString(tab + "\t\t") +
                "\n" + tab + "\t}";
    }
}